    function play(col)          {  sendRequestGET('play?column='+col)  }

    function startgame() {
     /**reset the colors of the
        tiles to white*/
        FarbenHerstellen()
        /**
         * check if there ara values in the text fields
         */
        const namea = document.getElementById("playerAname").value
        const colora = document.getElementById("playerAcolor").value
        const nameb = document.getElementById("playerBname").value
        const colorb = document.getElementById("playerBcolor").value
        if (colora !== "")
            document.getElementById('playerA').style.backgroundColor = colora
        if (colorb !== "")
            document.getElementById('playerB').style.backgroundColor = colorb
        if (namea !== "")
            document.getElementById("playerName1").innerHTML = namea
        if (nameb !== "")
            document.getElementById("playerName2").innerHTML = nameb
        /**
         * send Request with the specified infos for starting a game
         * */
        sendRequestGET('newgame?namea='+namea+'&colora='+colora+'&nameb='+nameb+'&colorb='+colorb+
        '&humanstarts='+document.getElementById("humanstarts").checked)
    }

    function undomove() {
        sendRequestGET('undo?')
    }

    function FarbenHerstellen() {
        let i;
        for (i = 1; i <= 7; i++)
        {
            for (let j = 1; j <= 6; j++)
            {
                document.getElementById('place('+i+','+j+')').style.backgroundColor = "white"
            }
        }
        for (i = 0; i <= 6; i++)
            document.getElementById('play'+i).disabled = false
        document.getElementById('testbutton').disabled = false
        document.getElementById("result").innerHTML = ""
        document.getElementById("playerSpielsteine1").innerHTML = "21"
        document.getElementById("playerSpielsteine2").innerHTML = "21"
    }

    const http = new XMLHttpRequest();
    const http2 = new XMLHttpRequest();

    /**
     * send Request for computer move
    * */
    function sendRequestGETForComputer(url) {
        http2.open('GET', url)
        http2.send()
    }

    function sendRequestGET(url = '') {
        http.open('GET', url);
        http.send()
    }

    http2.onreadystatechange = function load()
    {
        if (http2.readyState === 4 && http2.status === 200 && this.responseText !== "")
        {
            const infos = this.responseText.split(".")
            setzeInfos(infos[0], infos[1], infos[2], infos[3], infos[4], infos[5])
        }
    }

    http.onreadystatechange = function loadresult() {
        if (this.readyState === 4 && http.status === 200 && this.responseText.split(".")[0] === "deleted")
        {
            const infos = this.responseText.split(".")
            // deleted.place().SpielerID.Spielsteine
            document.getElementById(infos[1]).style.backgroundColor = "white"
            document.getElementById("playerSpielsteine"+infos[2]).innerHTML = infos[3]
            if (document.getElementById("PlayWithComputer").checked)
                sendRequestGETForComputer('computer?')
        }
        else if(this.readyState === 4 && http.status === 200 && this.responseText !== "")
        {
            //Platz-Spielername-Farbe-spielsteineübrig-Gewinner.SpielerID
            const infos = this.responseText.split(".")
            setzeInfos(infos[0], infos[1], infos[2], infos[3], infos[4], infos[5])

            /**
             * if user clicked the checkbox to play with the computer
             * then send reqeust for computer move
             * */
            if (document.getElementById("PlayWithComputer").checked)
                sendRequestGETForComputer('computer?')
        }
    }

    /**
     * update the board after playing a move
     * */
    function setzeInfos(platz ='', spielername ='', spielerfarbe ='',
                        spielsteineuebrig ='', gewinner ='', istSpielerADran = '') {
        console.log(gewinner)
        if (platz !== 'null')
            document.getElementById(platz).style.backgroundColor = spielerfarbe
        if (gewinner !== "null")
        {
            if (gewinner === "unentschieden")
                document.getElementById('result').innerHTML = "Das Spiel ist zu ende"
            else
                document.getElementById('result').innerHTML = gewinner + " hat gewonnen"
            for (let i = 0; i <= 6; i++)
                document.getElementById('play'+i).disabled = true
            document.getElementById('testbutton').disabled = true
        }
        if (spielsteineuebrig !== 'null')
        {
            document.getElementById("playerName"+istSpielerADran).innerHTML = spielername
            document.getElementById("playerSpielsteine"+istSpielerADran).innerHTML = spielsteineuebrig
        }
    }
/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */

import io.javalin.Javalin
import io.javalin.http.Context
import java.time.Duration
import java.time.LocalDateTime


class App {
    private lateinit var game: Game
    lateinit var currentPlayer:Spieler
    private var winner:String? = null
    private var humanstarts = false
    var time = ""
    override fun toString(): String {
        var s: String
        s = "${game.history.last()}_"
        s += currentPlayer.getname() + "_"
        s += currentPlayer.getfarbe() + "_"
        s += currentPlayer.spielsteine.toString() + "_"
        s += "${winner}_"
        s += "${currentPlayer.ID}_"
        s += "$time Sekunden"
        return s
    }
    init {
        /**
         * start the html website
         * */
        val app = Javalin.create{config ->
            config.addStaticFiles("/public")
        }.start(7070)

        /**
         * start a new game
         * */
        app.get("/newgame"){ctx: Context  ->
            var nameA = ctx.queryParam("namea")!!
            var nameB = ctx.queryParam("nameb")!!
            var colorA = ctx.queryParam("colora")!!
            var colorB = ctx.queryParam("colorb")!!
            humanstarts = ctx.queryParam("humanstarts")!!.toBoolean()
            /**
             *if the user has not entered anything
             * then set default infos
             * */
            if (nameA.isEmpty())
                nameA = "SpielerA"
            if (nameB.isEmpty())
                nameB = "SpielerB"
            if (colorA.isEmpty())
                colorA = "black"
            if (colorB.isEmpty())
                colorB = "yellow"
            game = Game.new(nameA, colorA, nameB, colorB, humanstarts)
            if (!humanstarts)
            {
                computerturn()
                ctx.result(toString())
            }
            else
                ctx.result("")
        }

        /**
         * run the tests
         * */
        app.get("/test"){ctx: Context  ->
            val takedtime = LocalDateTime.now()
            game.runTests()
            time = ""+Duration.between(takedtime, LocalDateTime.now()).toSeconds().toInt()
            ctx.result("")
        }

        /**
         * find the best move for the computer and make it
         * */
        app.get("/computer"){ctx: Context  ->
            if (!game.isGameOver() && winner == null)
            {
                val takedtime = LocalDateTime.now()
                computerturn()
                time = ""+Duration.between(takedtime, LocalDateTime.now()).toSeconds()
                if (time.toInt() < 1)
                    time = "0."+Duration.between(takedtime, LocalDateTime.now()).toMillis()
                ctx.result(toString())
                println("es hat $time " +
                        "seconden gedauert, bis der beste Zug übermittelt wurde..")
            }
        }

        /**
         * make the human move
         * */
        app.get("/play"){ctx: Context  ->
            val column = ctx.queryParam("column")!!.toInt()
            humanturn(column)
            /**
             * return the infos to play the move
             * */
            ctx.result(toString())
        }
        /**
         * undo the last move if the history contains any played move
         * */
        app.get("/undo"){ctx: Context  ->
            if (game.history.isNotEmpty())
            {
                ctx.result("deleted_${game.undo_move()}_${game.currentPlayer.ID}_" +
                        "${game.currentPlayer.spielsteine}")
            }
        }
    }

    fun humanturn(column: Int)
    {
        if (column in 0..6 && !game.isColumnFull(column))
        {
            currentPlayer = game.currentPlayer
            game.make_move(column)
            winner = game.winner()
            if (game.isGameOver() && winner == null)
                winner = "unentschieden"
        }
    }

    fun computerturn()
    {
        currentPlayer = game.currentPlayer
        game.computerTurn()
        /**
         * check if there is a winner
         * */
        winner = game.winner()
        /**
         * if the game  is over and nobody has won
         * */
        if (game.isGameOver() && winner == null)
            winner = "unentschieden"
    }
}

fun main(args: Array<String>) {
    App()
}

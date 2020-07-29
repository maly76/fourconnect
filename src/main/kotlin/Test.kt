import java.time.Duration
import java.time.LocalDateTime

class Test private constructor(depth: Int, result: Triple<Boolean,Int,String>) {
    init {
        /**
         * print the result on the console
         * */
        if (depth == 1 && result.first)
            println("Die Spiel-Engine kann im nächsten Zug gewinnen\n" +
                    "Der gewählte Zug ist im Spaltenindex ${result.second}\n")
        else if (depth == 2 && result.first)
            println("Die Spiel-Engine vereitelt eine unmittelbare Gewinnbedrohung des Gegners\n" +
                    "Der gewählte Zug ist im Spaltenindex ${result.second}\n")
        else if (depth == 3 && result.first)
            println("die Spiel-Engine kann im übernächsten Zug gewinnen\n" +
                    "Der gewählte Zug ist im Spaltenindex ${result.second}\n")
        else if (depth == 4 && result.first)
            println("Die Spiel-Engine kann eine Drohung, die den Gegner im übernächsten Zug " +
                    "ansonsten einen Gewinn umsetzen lässt, vereiteln\n" +
                    "Das Board:\n${result.third}")
        else if (depth == 5 && result.first)
            println("Die Spiel-Engine kann im überübernächsten Zug gewinnen\n" +
                    "Das Board:\n${result.third}")
    }

    companion object
    {
        /**
         * call the minimax for depth 1 to 5 to be able to run the tests
         * */
        fun runTests(board: Board)
        {
            val takedtime = LocalDateTime.now()
            println("|----------------------------------------|\n" +
                    "|              Tests running...          |\n" +
                    "|----------------------------------------|\n")
            for (i in 1..5)
            {
                println("Test $i")
                val res = checkValues(i, board)
                if (res.first)
                    Test(i, res)
                println("----------------------------------------------------")
            }
            println("|----------------------------------------|\n" +
                    "|              Tests finished...         |\n" +
                    "|----------------------------------------|\n")
            println("es hat ${Duration.between(takedtime, LocalDateTime.now()).toSeconds()} Seconden gedauert")
        }

        /**
         * return true if a test is successful
         * */
        private fun checkValues(depth: Int, board: Board): Triple<Boolean,Int, String>
        {
            /**
             * call the minimax and get the scores of the moves
             * */
            val values = Computer.new_test(board, depth)
            return when (depth) {
                1 -> Triple(values.first.keys.any { x -> x > 699 }, values.first[values.first.keys.max()!!]!!, values.third)
                2 -> Triple(values.first.keys.any { x -> x < 0 }, values.first[values.first.keys.max()!!]!!, values.third)
                3 -> Triple(values.first.keys.max()!! > 500, values.first[values.first.keys.max()!!]!!, values.third)
                4 -> Triple(values.second[3], values.first[values.first.keys.max()]!!, values.third)
                else -> Triple(depth == 5 && values.second[4], values.first[values.first.keys.max()]!!, values.third)
            }
        }
    }
}

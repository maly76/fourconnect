class Test private constructor(depth: Int, result: Boolean) {
    init {
        /**
         * print the result on the console
         * */

        println("Tests running...")
        if (depth == 1 && result)
            println("die Spiel-Engine kann im nächsten Zug gewinnen")
        else if (depth == 2 && result)
            println("Die Spiel-Engine vereitelt eine unmittelbare Gewinnbedrohung des Gegners")
        else if (depth == 3 && result)
            println("die Spiel-Engine kann im übernächsten Zug gewinnen")
        else if (depth == 4 && result)
            println("Die Spiel-Engine vereitelt eine Drohung, die den Gegner im übernächsten Zug " +
                    "ansonsten einen Gewinn umsetzen lässt")
        else if (depth == 5 && result)
            println("die Spiel-Engine kann im überübernächsten Zug gewinnen")
    }

    companion object
    {
        /**
         * call the minimax for depth 1 to 5 to be able to run the tests
         * */
        fun runTests(board: Board)
        {
            for (i in 1..5)         Test(i, checkValues(i, board))
        }

        /**
         * return true if a test is successful
         * */
        private fun checkValues(depth: Int, board: Board): Boolean
        {
            val c = Computer()
            /**
             * call the minimax and get the scores of the moves
             * */
            val values = c.runtest(board, depth)
            return when (depth) {
                1 -> values.any { x -> x > 80 }
                2 -> values.any { x -> x < 0 && x == -100 } && values.any{ y -> y > 0}
                3 -> values.any { x -> x > 50 }
                4 -> values.any { x -> x < 0 } && values.any{ y -> y in -90..-80 }
                else -> depth == 5 && values.any { x -> x > 0 }
            }
        }
    }
}


/**
 * this class helps to implement the minimax and find the best move
 * */
class Computer private constructor() : MiniMax
{
    override var gewuenshctetiefe: Int = 0
    override lateinit var moves_scores: HashMap<Int, Int>
    override lateinit var storedBoards:HashMap<Int, Triple<Int, Int, String>>
    override var tests_results = arrayOf(false, false, false, false, false)
    override var boardString: String = ""
    /**
     * get the HashMap with the boards and their evaluates from the database(text file 'evaluated_Boards.txt')
     * */
    override val database = Database.new("evaluated_Boards.txt").readHashmap()!!

    companion object{
        private val c = Computer()
        fun new_move(id:Int, _board: Board, depth: Int):Int
        {
            return c.bestmove(id, _board.deepcopy(), depth)
        }

        fun new_test(_board: Board, depth: Int): Triple<HashMap<Int,Int>,Array<Boolean>, String>
        {
            return c.runtest(_board.deepcopy(), depth)
        }
    }

    /**
     * call the minimax to find the best move
     * */
    override fun bestmove(id:Int, _board: Board, depth: Int): Int {
        /**
         * save the time when the algorithm starts
         * */
        /**
         * clear all infos there are saved
         * */
        gewuenshctetiefe = depth
        storedBoards = HashMap()
        moves_scores = HashMap()
        /**
         * call the minimax with the board of the computer player and the specified depth
         * */
        val move_score = minimax(depth, 1, _board, 0, false)
        //minimax(4, Int.MIN_VALUE, Int.MAX_VALUE, 1, _board, 5)
        /**
         * if desired write the boards with their values after calling the minimax to the database
         * */

        /**
         * get the current time, subtract the two times, and print the result as the spent time
         * for finding the best move
         * */

        /**
         * play a random move of the stored moves if there are many good moves
         * if not play a random one
         * */
        val score = when(id)
        {
            /**
             * if it is the computer turn then return the move with the max score
             * */
            0    -> moves_scores.keys.max()
            /**
             * if it is the human turn and he wants to find the best move for him
             * then return the move with the min score because he is the minimizing player
             * */
            else -> moves_scores.keys.min()
        }

        val move = when {
            moves_scores.size > 0 -> moves_scores[score]!!
            database.isNotEmpty() -> move_score.second
            else -> _board.randomMove()
        }
        val b = _board.toString().replace("\n", "_")

        /**
         * store the board in the database if the move was made for the computer and the board is not
         * stored
         * */
        if (!database.containsKey(hashBoard(_board)) && id == 0)
            Database.new("evaluated_Boards.txt")
                .writeToDatabase("${hashBoard(_board)},$score,$move,$b")
        return move
    }

    /**
     * call the minimax to run the test and return the scores
     * */
    fun runtest(_board: Board, depth: Int): Triple<HashMap<Int,Int>,Array<Boolean>, String>
    {
        boardString = ""
        tests_results = arrayOf(false, false, false, false, false)
        gewuenshctetiefe = depth
        storedBoards = HashMap()
        moves_scores = HashMap()
        minimax(depth, 1, _board, 0, true)
        return Triple(moves_scores,tests_results, boardString)
    }

    /**
     * give 100 if computer has won and -100 if enemy has won
     * else evaluate the board with the monte-carlo method after 450 randomly games
     * */
    override fun evaluate(board: Board): Int
    {
        val number = 700
        return when(VierInReihe(board))
        {
            1           ->      number * 100
            -1          ->      -number * 100
            else        ->      evaluateMoves(number, board.deepcopy()).max()!!
        }
    }

    fun evaluateMoves(number: Int, _board: Board): ArrayList<Int>
    {
        val moves = _board.list_moves()
        val statistiken = ArrayList<IntArray>()
        val values = ArrayList<Int>()
        for (move in moves)
        {
            _board.make_move(move)
            statistiken.add(simulatePlays(_board, number))
            _board.undo_move()
        }
        for (valueArray in statistiken)
        {
            values.add(valueArray[2])
        }
        return values
    }

    fun simulatePlays(_board: Board, number: Int): IntArray
    {
        assert(number >= 1)
        var counter = number
        val count = intArrayOf(0,0,0)
        while (counter > 0)
        {
            /**
             * make a copy of the board, play until one has won or the game is over
             * and save the result in the array count which contains three
             * values (losses, tie, wins)
             * */
            val b = _board.deepcopy()
            count[playrandomly(b)+1] += 1
            counter--
        }
        return count
    }

    /**
     * play a game with random moves until one wins or game is over
     * */
    fun playrandomly(_board: Board): Int
    {
        var value = VierInReihe(_board)
        while (value == 0)
        {
            val moves = _board.list_moves()
            if (moves.isEmpty())   return 0
            val randomMove = _board.randomMove()
            _board.make_move(randomMove)
            value = VierInReihe(_board)
        }
        return value
    }

    /**
     * check if there are four tokens in a row
     * */
    private fun VierInReihe(board: Board): Int
    {
        return when{
            board.isWin(board.bitBoards[0]) -> -1
            board.isWin(board.bitBoards[1]) -> 1
            else                            -> 0
        }
    }
}
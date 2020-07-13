import java.time.Duration
import java.time.LocalDateTime
/**
 * this class helps to implement the minimax and find the best move
 * */
class Computer() : MiniMax
{
    override var gewuenshctetiefe: Int = 0
    override lateinit var moves_scores: HashMap<Int, Int>
    override var col = 0
    override lateinit var board:Board
    /**
     * HashMap<Board, Pair<score, move>
     * */
    override lateinit var storedBoards:HashMap<Long, Pair<Int,Int>>
    override val test = ArrayList<Int>()
    lateinit var takedtime: LocalDateTime
    /**
     * get the HashMap with the boards and their evaluates from the database(text file 'evaluated_Boards.txt')
     * */
    override val database = Database.new("evaluated_Boards.txt").readHashmap()!!

    /**
     * call the minimax to find the best move
     * */
    override fun bestmove(id:Int, _board: Board, depth: Int): Int {
        /**
         * save the time when the algorithm starts
         * */
        takedtime = LocalDateTime.now()
        /**
         * clear all infos there are saved
         * */
        gewuenshctetiefe = depth
        this.board = _board.deepcopy()
        storedBoards = HashMap()
        moves_scores = HashMap()
        /**
         * call the minimax with the board of the computer player and the specified depth
         * */
        val i = minimax(depth, 1, this.board.bitBoards[0], 0)
        /**
         * if desired write the boards with their values after calling the minimax to the database
         * */
        //Database.new("evaluated_Boards.txt").writeToDatabase(storedBoards)

        /**
         * get the current time, subtract the two times, and print the result as the spent time
         * for finding the best move
         * */
        println("es hat ${Duration.between(takedtime, LocalDateTime.now()).toSeconds()} " +
                "seconden gedauert, bis der beste Zug übermittelt wurde..")
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
        return if (moves_scores.size > 1)
            moves_scores[score]!!
        else
            this.board.randomMove()
    }

    /**
     * call the minimax to run the test and return the scores
     * */
    fun runtest(_board: Board, depth: Int): IntArray
    {
        gewuenshctetiefe = depth
        this.board = _board.deepcopy()
        storedBoards = HashMap()
        moves_scores = HashMap()
        minimax(depth, 1, this.board.bitBoards[0], 0)
        return moves_scores.keys.toIntArray()
    }

    /**
     * give 100 if computer has won and -100 if enemy has won
     * else evaluate the board with the monte-carlo method after 450 randomly games
     * */
    override fun evaluate(): Int
    {
        var score = 0
        when (val result = VierInReihe(board)) {
            1 -> score += 100
            -1 -> score += -100
            else -> {
                val number = 500
                val values = evaluateMoves(number)
                val maxValue = values.max()

                score += when {
                    result == 1 -> 100
                    maxValue!! > number/2 + number/4      -> 80
                    maxValue > number/2                   -> 50
                    //maxValue > number/3                 -> 20
                    //maxValue in number/4..number/3      -> -50
                    else                                -> 0
                }
            }
        }
        return score
    }

    fun evaluateMoves(number: Int): ArrayList<Int>
    {
        val _board = board.deepcopy()
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
    private fun VierInReihe(_board: Board): Int
    {
        return when{
            board.isWin(_board.bitBoards[0]) -> -1
            board.isWin(_board.bitBoards[1]) -> 1
            else                            -> 0
        }
    }
}
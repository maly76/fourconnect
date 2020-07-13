interface MiniMax
{
    val board: Board
    val gewuenshctetiefe:Int
    var col: Int
    val moves_scores:HashMap<Int, Int>
    val  storedBoards: HashMap<Long, Pair<Int, Int>>
    val test: ArrayList<Int>
    val database: HashMap<Long, Pair<Int,Int>>

    fun evaluate(): Int
    fun bestmove(id: Int, _board: Board, depth: Int): Int

    /**
     * Computer move : X
     * Human move: O
     * */
    fun minimax(depth: Int, turn: Int, _board: Long, _move: Int):Pair<Int, Int>
    {
        /**
         * if a player has won or the depth is 0
         * */
        if (depth == 0 || board.isWin(board.bitBoards[0])
                || board.isWin(board.bitBoards[1]))
            return Pair(evaluate(), _move)

        /**
         * check if the board exists in the database or in the stored boards
         * to avoid redundant calls
         * */
        if (storedBoards.containsKey(turn*_board) || database.containsKey(turn*_board))
        {
            /**
             * get the value of the board if it is evaluated
             * */
            val value: Int = when{
                storedBoards.containsKey(turn*_board)  ->    storedBoards[turn*_board]!!.first
                else                              ->    database[turn*_board]!!.first
            }

            return Pair(value, _move)
            /**
             * convert the score for current player
             *
            return if (turn == 1)
                Pair(value, _move)
            else
                Pair(-value, _move)*/
        }

        /**
         * get possible moves
         * */
        val moves = board.list_moves()

        if (moves.isEmpty())    return Pair(0, _move)

        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE

        for (move in moves)
        {
            /**
             * if it is the computer turn then make the first move and call the minimax recursively
             * to find the best move
             * */
            if (turn == 1)
            {
                board.make_move(move)
                val currentscore = minimax(depth - 1, -1, board.bitBoards[1], move).first
                /**
                 * keep having the value of max if it is higher than the current score,
                 * if not get the value of current score
                 * */
                max = currentscore.coerceAtLeast(max)


                if (depth == gewuenshctetiefe)
                {
                    /**
                     * print the score of the move on the console to have a better view
                     * and add the move to the moves to chose later the best one
                     * */
                    println("computer score for position $move is: $currentscore")
                    moves_scores[currentscore] = move
                }

                if (currentscore >= 0)
                    if (depth == gewuenshctetiefe)
                        moves_scores[currentscore] = move

                /**
                 * if computer can won in the next move then break the for loop, because
                 * it is impossible to find a better move
                 * */
                if (currentscore == 100)
                {
                    board.undo_move()
                    break
                }

                /**
                 * if there is no good move in the list then store the last move of the list
                 * */
                if (moves.indexOf(move) == moves.size-1 && max < 0)
                    if (depth == gewuenshctetiefe)
                        moves_scores[currentscore] = move
            }
            else if (turn == -1)
            {
                board.make_move(move)

                val currentscore = minimax(depth - 1, 1, board.bitBoards[0], move).first
                min = currentscore.coerceAtMost(min)

                if (min == -100)
                {
                    board.undo_move()
                    break
                }
            }
            board.undo_move()
        }

        /**
         * store the move
         * */
        storedBoards[turn*_board] = when(turn)
        {
            1       ->  Pair(max, _move)
            else    ->  Pair(min, _move)
        }

        /**
         * return max if it is the computer turn and min if it's the enemy turn
         * */
        return if (turn == 1)   Pair(max, _move)
        else    Pair(min, _move)
    }
}
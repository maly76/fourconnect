interface MiniMax
{
    val gewuenshctetiefe:Int
    val moves_scores:HashMap<Int, Int>
    val  storedBoards: HashMap<Int, Triple<Int, Int, String>>
    val database: HashMap<Int, Triple<Int,Int, String>>
    val tests_results: Array<Boolean>
    var boardString: String
    fun evaluate(board: Board): Int
    fun bestmove(id: Int, _board: Board, depth: Int): Int
    fun hashBoard(board: Board): Int = TranspositionTable.new_Hash(board)

     /** Computer move : X
     * Human move: O
     * */


    fun minimax(depth: Int, turn: Int, board: Board, _move: Int
        , is_a_Test: Boolean):Pair<Int, Int>
    {

         /** if a player has won or the depth is 0
         * */

        if (depth == 0 || board.isWin(board.bitBoards[0])
                || board.isWin(board.bitBoards[1]))
            return Pair(evaluate(board), _move)

         /** check if the board exists in the database or in the stored boards
         * to avoid redundant calls
         * */

        if (storedBoards.containsKey(hashBoard(board)) ||
                (database.containsKey(hashBoard(board)) && (!is_a_Test ||
                (is_a_Test && storedBoards.isNotEmpty()))))
        {

             /** get the value of the board if it is evaluated
             **/
            val value: Int = when{
                storedBoards.containsKey(hashBoard(board))  ->    storedBoards[hashBoard(board)]!!.first
                else                              ->    database[hashBoard(board)]!!.first
            }
            val move: Int = when{
                storedBoards.containsKey(hashBoard(board))  ->    storedBoards[hashBoard(board)]!!.second
                else                              ->    database[hashBoard(board)]!!.second
            }

            return Pair(value, move)
        }

         /** get possible moves
         * */

        val moves = board.list_moves()

        if (moves.isEmpty())    return Pair(0, _move)

        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE

        for (move in moves)
        {

             /** if it is the computer turn then make the first move and call the minimax recursively
             * to find the best move
             * */

            if (turn == 1)
            {
                board.make_move(move)

                val currentscore = minimax(depth - 1, -1, board, move, is_a_Test).first


                 /** keep having the value of max if it is higher than the current score,
                 * if not get the value of current score
                 **/

                max = currentscore.coerceAtLeast(max)
                if (depth == gewuenshctetiefe)
                {

                     /** print the score of the move on the console to have a better view
                     * and add the move to the moves to chose later the best one
                     **/

                    if (!is_a_Test)
                        println("computer score for position $move is: $currentscore")
                    moves_scores[currentscore] = move
                }

                /*if (currentscore >= 0)
                    if (depth == gewuenshctetiefe)
                        moves_scores[currentscore] = move*/


                 /** if computer can won in the next move then break the for loop, because
                 * it is impossible to find a better move
                 * */

                if (currentscore > 1000)
                {
                    /*if (depth == gewuenshctetiefe)
                    {
                        if (board.isWin(board.bitBoards[0]))
                            moves_scores[currentscore] = move
                    }*/

                    if (gewuenshctetiefe == 5 && is_a_Test && !tests_results[4]){
                        tests_results[4] = true
                        boardString = board.toString()
                        break
                    }
                    board.undo_move()
                    break
                }


                 /** if there is no good move in the list then store the last move of the list
                 * */

                /*if (move == moves.last() && max < 0)
                    if (depth == gewuenshctetiefe)
                        moves_scores[currentscore] = move*/
            }
            else if (turn == -1)
            {
                board.make_move(move)

                val currentscore = minimax(depth - 1, 1, board, move, is_a_Test).first
                min = currentscore.coerceAtMost(min)
                if (min < -1000)
                {
                    if (gewuenshctetiefe == 4 && is_a_Test && !tests_results[3]){
                        tests_results[3] = true
                        boardString = board.toString()
                        break
                    }
                    board.undo_move()
                    break
                }
            }
            board.undo_move()
        }

         /** store the board
         * */

        storedBoards[hashBoard(board)] = when(turn)
        {
            1       ->  Triple(max, _move, board.toString())
            else    ->  Triple(min, _move, board.toString())
        }

         /** return max if it is the computer turn and min if it's the enemy turn
         * */

        return if (turn == 1)   Pair(max, _move)
               else    Pair(min, _move)
    }
}

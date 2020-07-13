class Board()
{
    var bitBoards = arrayOf(0b0000000000000000000000000000000000000000000000000L, 0b0000000000000000000000000000000000000000000000000L)
    var height = intArrayOf(0,7,14,21,28,35,42)
    val columns = intArrayOf(0,7,14,21,28,35,42)
    var moves = IntArray(42)
    var counter = 0

    /**
     * a secondary constructor to be able to copy the board
     * it is private and can be called only when making a copy
    **/
    private constructor(_bitboards:Array<Long>, _height: IntArray, _moves: IntArray, _counter: Int):this()
    {
        bitBoards = _bitboards
        height = _height
        moves = _moves
        counter = _counter
    }

    /**
     * convert the long value(which is the board) to binary form,
     * to draw the board on the console
     * */
    fun Long.toBinary(len: Int): String {
        return String.format("%" + len + "s", this.toString(2)).replace(" ".toRegex(), "0")
    }

    /**
     * check if a column is full
     * */
    fun isColumnFull(col: Int): Boolean
    {
        return height[col] == columns[col] + 6
    }

    fun isGameOver(): Boolean
    {
        return list_moves().isEmpty()
    }

    fun make_move(col: Int)
    {
        val move = 1L shl  height[col]++
        bitBoards[counter and 1] = bitBoards[counter and 1] xor move
        moves[counter++] = col
    }

    fun undo_move()
    {
        val col: Int = moves[--counter]
        val move = 1L shl --height[col]
        bitBoards[counter and 1] = bitBoards[counter and 1] xor move
    }

    fun isWin(_bitboard: Long): Boolean {
        val directions = intArrayOf(1, 7, 6, 8)
        var bb: Long
        for (direction in directions) {
            bb = _bitboard and (_bitboard shr direction)
            if (bb and (bb shr 2 * direction) != 0L) return true
        }
        return false
    }

    fun randomMove(): Int = list_moves().random()

    fun list_moves(): ArrayList<Int>
    {
        val moves = ArrayList<Int>()
        val TOP = 0b1000000_1000000_1000000_1000000_1000000_1000000_1000000L
        for (col in 0..6) {
            if (TOP and (1L shl height[col]) == 0L)
                moves.add(col)
        }
        return moves
    }

    /**
     * draw the board and put the moves at the correct positions
     * */
    fun drawBoard(a: CharArray): String
    {
        var s = ""
        val table = ArrayList<ArrayList<Char>>()
        var counter = 0
        for (i in 0..6)
        {
            table.add(ArrayList())
            for (j in 0..6)
            {
                table[i].add(a[counter])
                counter++
            }
        }
        for (i in 0..6)
        {
            for (j in 6 downTo 0)
            {
                s += table[j][i]
            }
            s += "\n"
        }
        return s
    }

    /**
     * get the index of the last played move in a column
     * */
    fun getMoveIndex(col: Int):Int = height[col] - columns[col]

    /**
     * convert the longs to binary form,
     * create the common board and draw it
     * */
    override fun toString(): String {
        var b = bitBoards[0].toBinary(49).toCharArray()
        var board1 = ""
        for (char in b)
        {
            if (char == '1')
                board1 += "O"
            else
                board1 += '-'
        }
        val b1 = board1.toCharArray()
        b = bitBoards[1].toBinary(49).toCharArray()
        for (char in 0..b.size-1)
        {
            if (b[char] == '1')
                b1[char] = 'X'
        }
        return drawBoard(b1)
    }

    /**
     * a methode to deepcopy the board
     * 1- create a copy of the variables and arrays and give back a copied board
     * */
    fun deepcopy(): Board = Board(bitBoards.clone(), height.clone(), moves.clone(), counter)
}
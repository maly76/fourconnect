/**
 * this class may have redundant methods which can be called in the App class for
 * example toString or make_move, but I think it
 * is better to make the board only here accessible to have a controlled data structure
 * */

class Game private constructor(_board: Board, _players:ArrayList<Spieler>)
{
    private var board:Board
    private val players: ArrayList<Spieler>
    val history = ArrayList<Place>()
    /**
     * return the current player
     * */
    val currentPlayer: Spieler
    get() {
        return if (players[0].istDran)
            players[0]
        else
            players[1]
    }
    init {
        assert(_players.isNotEmpty())
        players = _players
        board = _board
    }

    companion object
    {
        /**
         * a static methode to create tow players and a new game
         * */
        fun new(nameA: String, farbeA: String, nameB: String, farbeB: String, humanstarts: Boolean):Game
        {
            val players: ArrayList<Spieler> = ArrayList()
            val board = Board()
            players.add(Spieler(nameA, farbeA, 1))
            players.add(Spieler(nameB, farbeB, 2))
            if (!humanstarts)
                players[0].istDran = true
            else
                players[1].istDran = true
            return Game(board, players)
        }
    }

    /**
     * run the five tests
     * */
    fun runTests()
    {
        Test.runTests(board)
    }

    /**
     * find the best move by calling the minimax and make it
     * */
    fun computerTurn(): Place
    {
        assert(!isGameOver())
        val col = Computer.new_move(players.indexOf(currentPlayer), board, 3)
        return make_move(col)
    }

    /**
     * make a move and change the turn
     * */
    fun make_move(col: Int): Place
    {
        board.make_move(col)
        if (players[0].istDran)
            players[0].spielsteine--
        else
            players[1].spielsteine--
        players.forEach { x -> x.istDran = !x.istDran }
        val place = Place(col+1, board.getMoveIndex(col))
        history.add(place)
        return place
    }

    /**
     * undo a move and change the turn
     * */
    fun undo_move(): Place
    {
        board.undo_move()
        players.forEach { x -> x.istDran = !x.istDran }
        if (players[0].istDran)
            players[0].spielsteine++
        else
            players[1].spielsteine++
        val place = history.last()
        history.remove(history.last())
        return place
    }

    fun isGameOver(): Boolean = board.isGameOver()

    fun isColumnFull(col: Int): Boolean = board.isColumnFull(col)

    /**
     * if there is a winner return his name, if there is none return null
     * */
    fun winner(): String?
    {
        return when {
            board.isWin(board.bitBoards[0]) -> players[0].getname()
            board.isWin(board.bitBoards[1]) -> players[1].getname()
            else -> null
        }
    }

    /**
     * draw the current board
     * */
    override fun toString(): String = board.toString()
}
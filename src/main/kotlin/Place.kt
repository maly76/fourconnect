/**
 * representation of the move to find the position in the board
 * place(column, line)
 * line and columns start always with 1 to make the place better readable for others
 * */

class Place(_spalte:Int, _reihe:Int)
{
    var reihe = _reihe
    var spalte = _spalte
    override fun toString(): String {
        return "place($spalte,$reihe)"
    }
}
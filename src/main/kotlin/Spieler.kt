/**
 * a player has a name, a color and an ID and the number of tokens the player has
 * */
class Spieler(_name:String, _farbe:String, _id: Int)
{
    private var name = _name
    private val farbe = _farbe
    val ID = _id
    var istDran: Boolean = false
        get() {return field }
        set(value) { field = value}
    fun getfarbe():String{   return farbe }
    fun getname():String{   return name }
    var spielsteine: Int = 21
}
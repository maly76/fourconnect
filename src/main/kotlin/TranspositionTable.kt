import kotlin.random.Random

class TranspositionTable private constructor()
{
    val board = Board()
    companion object
    {
        private val database = Database.new("TranspositionTable.txt")
        fun new_Hash(board: Board): Int
        {
            val table = database.readTranspositionTable()!!
            var h = 0
            val s = board.toString().replace("\n", "-")
                    .replace("X", "0")
                    .replace("O", "1").toCharArray()
            for (i in s.indices)
            {
                if (s[i] != '-')
                    h = h xor table[i][s[i].toString().toInt()]
            }
            return h
        }
    }

    fun inittable(): Array<Array<Int>>
    {
        val database = Database.new("TranspositionTable.txt")
        var table = arrayOf<Array<Int>>()
        var counter = 1
        for (i in 0..55)
        {
            var l = arrayOf<Int>()
            for (j in 0..1)
            {
                l += Random.nextInt()
                counter++
            }
            table += l
        }
        database.writeToDatabase(table)
        return table
    }
}
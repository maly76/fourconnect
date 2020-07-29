import java.io.File
import java.lang.Exception


/**
 * this class may have functions which I don't need for this project, but I think it is very useful
 * for controlling a database
 * */

class Database private constructor(_file: File)
{
    private val file = _file
    companion object {
        /**
         * create a new file with the specified filename and return true
         * if the file with this name already exists then return this file as a database
         * */
        private lateinit var file: File
        fun new(filename: String): Database {
            file = File(filename)
            val isFileCreated = file.createNewFile()
            return if (isFileCreated)
            {
                Database(file)
            }
            else
            {
                Database(File(filename))
            }

        }
    }

    /**
     * delete the text file
     * */
    fun delete(): Boolean
    {
        return file.delete()
    }

    /**
     * write a HashMap with boards and their scores to the database
     * this is a rewriting of the database, so the old content will be removed after calling this method
     * */
    fun writeToDatabase(a: HashMap<Int, Triple<Int,Int,String>>)
    {
        var s = ""
        a.forEach{ x ->
            s += ("${x.key},${x.value.first},${x.value.second}," +
                    "${x.value.third.replace("\n", "?")}\n")
        }
        file.appendText(s)
        /**file.bufferedWriter().use { out ->
            a.forEach{ x ->
                out.append("${x.key},${x.value.first},${x.value.second}\n")
            }
        }*/
    }

    fun writeToDatabase(a: Array<Array<Int>>)
    {
        file.bufferedWriter().use { out ->
            a.forEach{ x ->
                out.append("${x[0]},${x[1]}\n")
            }
        }
    }

    /**
     * write a string value to the database
     * */
    fun writeToDatabase(s: String)
    {
        file.appendText("\n$s")
    }

    /**
     * return the content of the database as a string value
     * */
    fun readContent(): String
    {
        var s = ""
        file.forEachLine { x -> s += "$x\n" }
        return s
    }

    /**
     * return the HashMap of the stored boards and their scores if there are any, if not return null
     * */
    fun readHashmap():HashMap<Int, Triple<Int, Int, String>>?
    {
        return try {
            val values = HashMap<Int, Triple<Int, Int, String>>()
            var index: Int
            var b = ""
            file.forEachLine { x ->
                if (x.contains(','))
                {
                    val array = x.split(",")
                    index = array[0].toInt()
                    values[index] = Triple(array[1].toInt(), array[2].toInt(), array[3]
                            .replace("?", "\n"))
                }
            }
            values
        } catch (e: Exception) {
            null
        }
    }

    fun readTranspositionTable(): Array<Array<Int>>?
    {
        return try {
            var values = arrayOf<Array<Int>>()
            file.forEachLine { x ->
                val array = x.split(",")
                values += arrayOf(array[0].toInt(), array[1].toInt())
            }
            values
        }
        catch (e: Exception){
            null
        }
    }
}
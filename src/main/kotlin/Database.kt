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
                println("$filename is created successfully")
                Database(file)
            }
            else
            {
                println("$filename already exists!!")
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
    fun writeToDatabase(a: HashMap<Long, Pair<Int,Int>>)
    {
        file.bufferedWriter().use { out ->
            a.forEach{ x ->
                out.append("${x.key},${x.value.first}, ${x.value.second}\n")
            }
        }
    }

    /**
     * write a string value to the database
     * */
    fun writeToDatabase(s: String)
    {
        file.appendText("\n $s")
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
    fun readHashmap():HashMap<Long, Pair<Int, Int>>?
    {
        return try {
            val values = HashMap<Long, Pair<Int, Int>>()
            file.forEachLine { x ->
                val array = x.split(",")
                values[array[0].toLong()] = Pair(array[1].toInt(), array[2].toInt())
            }
            values
        } catch (e: Exception) {
            null
        }
    }
}
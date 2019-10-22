package calculator

import java.io.File as IoFile

/**
 * Implementation of [Reader] interface supplying strings from a file
 * @property fileName Filename of source file
 */
class FileReader(val fileName:String): Reader {
    var file: IoFile = IoFile(fileName)
    /**
     * @return String representation of file contents
     */
    override fun read() = file.readText()
}
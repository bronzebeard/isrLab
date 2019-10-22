package calculator

/**
 * [Writer] implementation directing the output into stdout
 */
class ConsoleWriter:Writer {
    override fun write(msg:String) {println(msg)}
}
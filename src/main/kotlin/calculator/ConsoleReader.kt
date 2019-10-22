package calculator

/**
 * Implementation of [Reader] interface supplying strings from a console input
 */
class ConsoleReader : Reader {
    override fun read() = readLine()!!
}
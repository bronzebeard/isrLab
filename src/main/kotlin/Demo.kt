import calculator.*

fun main() {
    val consReader = ConsoleReader()
    val calcer = Calculator(consReader)
    println(calcer.calculate())
}
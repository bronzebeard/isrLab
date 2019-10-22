package calculator

/**
 * Contains user interaction logic for [ExpressionParser]
 * @property reader Supplier of strings representing expressions
 * @property variableReader Supplier of strings representing lists of variables
 * @property writer Output redirector
 */
class Calculator(
    var reader: Reader = ConsoleReader(),
    var variableReader: Reader = ConsoleReader(),
    var writer: Writer = ConsoleWriter()
) {
    /**
     * Passes the string supplied by [Reader] to [ExpressionParser.parse]
     * @return Functional value representing the expression
     */
    fun getFunction(): IntFun {
        return ExpressionParser().parse(reader.read())

    }

    /**
     * Accepts [String] representation of variables list from [variableReader] and uses it to calculate the function supplied by [getFunction]
     * @return Int result of the expression calculated using user-supplied values
     */
    fun calculate(): Int {
        writer.write("Type variable values separated with spaces")
        var x: List<Int> = emptyList()
        val tmp = variableReader.read()
            if (tmp.isNotEmpty())
                x = tmp.replace(",", " ")
                    .replace(";", " ")
                    .split(" ")
                    .map { it.toInt() }
        writer.write("Type the expression in")
            return getFunction()(x)

    }

}
package calculator

import java.lang.Math.abs
import java.util.*
import kotlin.math.pow


typealias Fun<T> = (List<T>) -> T
typealias IntFun = Fun<Int>
typealias ReceiverFun<T> = T.(T) -> T
typealias ReceiverIntFun = ReceiverFun<Int>

/**
 * Class containing expression parsing logic
 */
class ExpressionParser {

    private var i: Int = 0
    private var str = ""
    /**
     * Parses supplied expression
     * @param expression String representation of the expression for parsing
     * @throws InputMismatchException
     * @return Functional value representing the expression
     */
    fun parse(expression: String): IntFun {

        i = 0
        str = expression.replace("\\s".toRegex(), "")
        if (str.isNotEmpty())
            return lowPrior()
        else throw InputMismatchException("Empty string for evaluation")
    }

    private fun lowPrior(): IntFun {

        var firstArg = upperLowPrior()

        while (i < str.length && str[i] != ')') {
            if (str[i] == '>') {
                i += 2
                firstArg = composeBiFun(firstArg, upperLowPrior(),Int::shr)
            } else if (str[i] == '<') {
                i += 2
                firstArg = composeBiFun(firstArg, upperLowPrior(),Int::shl)
            }
        }

        if (i < str.length && str[i] == ')') {
            i++
        }

        return firstArg
    }

    private fun upperLowPrior(): IntFun {

        var firstArg = mediumPrior()
        var secondArg: IntFun
        while (i < str.length && str[i] != ')' && str[i] != '<' && str[i] != '>') {
            if (str[i] == '+') {
                i++
                secondArg = mediumPrior()
                firstArg = composeBiFun(firstArg, secondArg, Int::plus)
                //{ x, y, z -> firstArg(x, y, z) + secondArg(x, y, z) }
                //Add(a, mediumPrior())
            } else if (str[i] == '-') {
                i++
                secondArg = mediumPrior()
                firstArg = composeBiFun(firstArg, secondArg, Int::minus)
            }

        }

        return firstArg
    }

    private fun mediumPrior(): IntFun {

        var firstArg = highPrior()
        var secondArg: IntFun
        while (i < str.length && str[i] != ')' && str[i] != '+' && str[i] != '-' && str[i] != '<' && str[i] != '>') {
            if (str[i] == '*') {
                i++
                secondArg = highPrior()
                firstArg = composeBiFun(firstArg, secondArg, Int::times)
            } else if (str[i] == '/') {
                i++
                secondArg = highPrior()
                firstArg = composeBiFun(firstArg, secondArg, Int::div)
            }

        }

        return firstArg
    }

    private fun highPrior(): IntFun {

        var num = 0
        val arg: IntFun
        when (str[i]) {
            '-' -> {
                i++
                arg = highPrior()
                return { x -> arg(x) * (-1) }
            }


            '(' -> {
                i++
                return lowPrior()
            }

            else -> {
                if (Character.isDigit(str[i])) {
                    while (i < str.length && Character.isDigit(str[i])) {
                        num = num * 10 + Integer.valueOf(Character.toString(str[i]), 10)
                        i++
                    }

                    return { num }
                } else throw InputMismatchException("Incorrect expression format")
            }
        }
    }

    private fun composeBiFun(firstArg: IntFun, secondArg: IntFun, func: ReceiverIntFun): IntFun {
        return fun(x): Int {
            return firstArg(x).func(secondArg(x))
        }
    }
}
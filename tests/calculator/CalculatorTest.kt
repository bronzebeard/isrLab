
import calculator.Calculator
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*
import kotlin.math.pow
import kotlin.test.Test as KotlinTest
import kotlin.test.assertFailsWith

typealias SR = StubReader
typealias SW = StubWriter

const val LOWERBOND = -10000
const val UPPERBOND = 1000

internal class CalculatorTest {
    val i = (LOWERBOND..0).toList()
    val j = (0..UPPERBOND).toList()
    @Test
    fun multiplicationTests() {
        runTest(Int::times, "*", i, j)
        //Edge-cases
        assertEquals(
            Int.MIN_VALUE * Int.MAX_VALUE,
            Calculator(SR("" + Int.MIN_VALUE + "*" + Int.MAX_VALUE), SR(), SW()).calculate()
        )
    }
    @Test
    fun shiftRightTests() {
        runTest(Int::shr, ">>",i,j)
        assertEquals(2048,Calculator(SR("1024>>(1+2)<<(1+1)*2"),SR(),SW()).calculate())
    }
    @Test
    fun shiftLeftTests() {
        runTest(Int::shl, "<<",i,j)
    }

    @Test
    fun divisionTests() {
        runTest(Int::div, "/", i, j.drop(1))
        //Edge-cases
        assertEquals(
            Int.MIN_VALUE / Int.MAX_VALUE,
            Calculator(SR("" + Int.MIN_VALUE + "/" + Int.MAX_VALUE), SR(), SW()).calculate()
        )
    }

    @Test
    fun additionTests() {
        runTest(Int::plus, "+", i, j)
        //Edge-cases
        assertEquals(
            Int.MIN_VALUE + Int.MAX_VALUE,
            Calculator(SR("" + Int.MIN_VALUE + "+" + Int.MAX_VALUE), SR(), SW()).calculate()
        )
    }

    @Test
    fun substractionTest() {
        runTest(Int::minus, "-", i, j)
        //Edge-cases
        assertEquals(
            Int.MIN_VALUE - Int.MAX_VALUE,
            Calculator(SR("" + Int.MIN_VALUE + "-" + Int.MAX_VALUE), SR(), SW()).calculate()
        )
    }
    @Test
    fun powTest() {
        runTest({x:Int,y:Int -> x.toDouble().pow(y).toInt()},"^",i,j)
    }

    @Test
    fun absTest() {
        i.forEachParallel {
            assertEquals(kotlin.math.abs(it), Calculator(SR("abs $it"), SR(), SW()).calculate())
        }
        j.forEachParallel {
            assertEquals(kotlin.math.abs(it), Calculator(SR("abs $it"), SR(), SW()).calculate())
        }
        //Edge-cases
        assertEquals(
            kotlin.math.abs(Int.MIN_VALUE),
            Calculator(SR("abs" + Int.MIN_VALUE), SR(), SW()).calculate()
        )

    }

    @Test
    fun squareTest() {
        i.forEachParallel {
            assertEquals((it.toDouble().pow(2).toInt()), Calculator(SR("s $it"), SR(), SW()).calculate())
        }
        j.forEachParallel {
            assertEquals((it.toDouble().pow(2).toInt()), Calculator(SR("s $it"), SR(), SW()).calculate())
        }

        assertEquals(
            (Int.MIN_VALUE).toDouble().pow(2).toInt(),
            Calculator(SR("s" + Int.MIN_VALUE), SR(), SW()).calculate()
        )
    }

    @Test
    fun varTest() {
        assertEquals(
            5,
            Calculator(SR("(x0+x2)/x1"), SR("5,2,6"), SW()).calculate()
        )
        assertEquals(
            -5,
            Calculator(SR("x5+x3-(x0+x2)/x1"), SR("5,2,6,-6 777;6"), SW()).calculate()
        )
    }
    @KotlinTest
    fun inputTest() {
        assertFailsWith<InputMismatchException> {
            Calculator(SR(")("),SR(),SW()).calculate()}
        assertFailsWith<InputMismatchException> {
            Calculator(SR("()"),SR(),SW()).calculate()}
        assertFailsWith<InputMismatchException> {
            Calculator(SR("      "),SR(),SW()).calculate() }
        assertFailsWith<InputMismatchException> {
            Calculator(SR("xx"),SR(),SW()).calculate() }
        assertFailsWith<InputMismatchException> {
            Calculator(SR("x99"),SR(),SW()).calculate() }
    }


    private fun <A> Collection<A>.forEachParallel(f: suspend (A) -> Unit): Unit = runBlocking {
        map { async(Dispatchers.Default) { f(it) } }.forEach { it.await() }
    }

    private fun runTest(exFun: (Int,Int) -> Int, strRep: String, first: List<Int>, second: List<Int>) {
        first.forEachParallel { cur ->
            second.forEach {
                assertEquals(
                    exFun(cur,it),
                    Calculator(SR("" + cur + strRep + it), SR(), SW()).calculate()
                )
            }
        }
    }

}
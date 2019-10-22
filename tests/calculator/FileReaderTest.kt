import calculator.FileReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

const val STRING_LENGTH = 100;

internal class FileReaderTest {
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + (' ') + ','
    @Test
    fun read() {
        GlobalScope.launch(Dispatchers.IO) {
            for (i in 0..1000) {
                val randomString = (1..STRING_LENGTH)
                    .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")
                val tmpFile = createTempFile("ReaderTest$i", ".txt", File("./tmp"))
                tmpFile.writeText(randomString)
                val tmpReader = FileReader(tmpFile.path)
                assertEquals(randomString, tmpReader.read())
                tmpFile.delete()
            }
        }
    }

}
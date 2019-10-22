import calculator.Reader

class StubReader(var res:String=""): Reader {
    override fun read() = res
}
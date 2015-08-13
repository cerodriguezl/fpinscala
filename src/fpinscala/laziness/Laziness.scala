package fpinscala.laziness

object Lazyness {

    def main(args: Array[String]) {
        val stream = Stream(1, 2, 3, 4, 5, 6, 7, 8)
        println(stream.forAll(_ < 3))
        println(stream.forAll(_ < 10))
        println(stream.forAll(_ < 0))
    }

}
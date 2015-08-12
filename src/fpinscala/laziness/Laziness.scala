package fpinscala.laziness

object Lazyness {

    def main(args: Array[String]) {
        val stream = Stream(1, 2, 3, 4, 5, 6, 7, 8)
        println(stream.takeWhile(_ < 3).toList)
        println(stream.takeWhile(_ < 10).toList)
        println(stream.takeWhile(_ < 0).toList)
    }

}
package fpinscala.laziness

object Lazyness {

    def main(args: Array[String]) {
        val stream = Stream(1, 2, 3, 4, 5, 6, 7, 8)
        println(stream.map(_ * 1.5).toList)
    }

}
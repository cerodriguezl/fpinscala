package fpinscala.laziness

object Lazyness {

    def main(args: Array[String]) {
        val stream = Stream(1, 2, 3)
        val list = stream.toList
        println(list)
    }

}
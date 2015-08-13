package fpinscala.laziness

object Lazyness {

    def main(args: Array[String]) {
        val a = Stream.constant(3)
        println(a.take(12).toList)
    }

}
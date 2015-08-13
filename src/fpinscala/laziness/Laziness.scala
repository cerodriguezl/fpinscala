package fpinscala.laziness

object Lazyness {

    def main(args: Array[String]) {
        val a = Stream.constant(2)
        println(a)
    }

}
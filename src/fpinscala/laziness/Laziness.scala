package fpinscala.laziness

object Lazyness {

    def main(args: Array[String]) {
        val a = Stream.fibs()
        println(a)
        println(a.take(10).toList)
    }

}
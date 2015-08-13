package fpinscala.laziness

object Lazyness {

    def main(args: Array[String]) {
        val a = Stream.fibs()
        println(a.take(12).toList)
    }

}
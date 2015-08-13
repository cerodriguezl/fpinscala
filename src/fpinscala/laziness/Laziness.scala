package fpinscala.laziness

object Lazyness {

    def main(args: Array[String]) {
        val a = Stream.from(10)
        println(a.take(10).toList)
    }

}
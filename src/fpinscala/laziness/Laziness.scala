package fpinscala.laziness

object Lazyness {

    def main(args: Array[String]) {
        val stream = Stream(1, 2, 3, 4, 5, 6, 7, 8)
        val filtered = stream.filter(_ % 2 == 0)
        println(filtered)
        println(filtered.toList)
    }

}
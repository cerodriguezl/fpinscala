package fpinscala.laziness

object Lazyness {

    def main(args: Array[String]) {
        val a = Stream(1, 2, 3, 4)
        val b = Stream(5, 6, 7, 8)
        val c = a.append(b)
        val d = b.append(a)
        println(c)
        println(d)
        println(c.toList)
        println(d.toList)
    }

}
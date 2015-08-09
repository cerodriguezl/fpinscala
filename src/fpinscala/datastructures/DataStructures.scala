package fpinscala.datastructures

object Test {
    def main(args: Array[String]) {
        val a = List(1, 2, 3)
        val b = List(4, 5, 6)
        val l = List.zipWith(a, b)(_ + _)
        println(l)
    }
}
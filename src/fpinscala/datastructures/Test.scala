package fpinscala.datastructures

object Test {
    def main(args: Array[String]) {
        val a = List(1, 2, 3, 4, 5, 6)
        val l = List.filterViaFlatMap(a)(_ % 2 == 0)
        println(l)
    }
}
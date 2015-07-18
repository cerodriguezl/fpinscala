package fpinscala.datastructures

object Test {
    def main(args: Array[String]) {
        val l = List(1, 2, 3, 4, 5, 6)
        println(List.filter(l)(_ % 2 == 0))
    }
}
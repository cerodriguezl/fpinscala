package fpinscala.datastructures

object Test {
    def main(args: Array[String]) {
        val l = List.flatMap(List(1,2,3))(i => List(i,i))
        println(l)
    }
}
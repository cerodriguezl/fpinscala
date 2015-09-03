package fpinscala.state

import scala.annotation.tailrec

trait RNG {
    def nextInt: (Int, RNG)
}

object RNG {

    case class Simple(seed: Long) extends RNG {
        def nextInt: (Int, RNG) = {
            val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
            val nextRNG = Simple(newSeed)
            val n = (newSeed >>> 16).toInt
            (n, nextRNG)
        }
    }

    type Rand[+A] = RNG => (A, RNG)

    val int: Rand[Int] = _.nextInt

    def unit[A](a: A): Rand[A] =
        rng => (a, rng)

    def map[A, B](s: Rand[A])(f: A => B): Rand[B] =
        flatMap(s)(a => unit(f(a)))

    def nonNegativeInt: Rand[Int] = rng => {
        val (n, r) = rng.nextInt
        (if (n < 0) -(n + 1) else n, r)
    }

    def double: Rand[Double] = map(nonNegativeInt)(_ / (Int.MaxValue.toDouble + 1))

    def intDouble(rng: RNG): ((Int, Double), RNG) = {
        val (i, rng1) = rng.nextInt
        val (d, rng2) = double(rng1)
        ((i, d), rng2)
    }

    def doubleInt(rng: RNG): ((Double, Int), RNG) = {
        val ((i, d), rng1) = intDouble(rng)
        ((d, i), rng1)
    }

    def double3(rng: RNG): ((Double, Double, Double), RNG) = {
        val (a, rng1) = double(rng)
        val (b, rng2) = double(rng1)
        val (c, rng3) = double(rng2)
        ((a, b, c), rng3)
    }

    def ints(count: Int): Rand[List[Int]] = {
        sequence(List.fill(count)(int))
    }

    def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
        flatMap(ra)(a => map(rb)(b => f(a, b)))

    def sequence[A](fs: List[Rand[A]]): Rand[List[A]] =
        fs.foldRight(unit(List(): List[A]))((elem, acc) => map2(elem, acc)(_ :: _))

    def flatMap[A, B](f: Rand[A])(g: A => Rand[B]): Rand[B] = rng => {
        val (a, rng2) = f(rng)
        g(a)(rng2)
    }

    def nonNegativeLessThan(n: Int): Rand[Int] =
        flatMap(nonNegativeInt) { i =>
            val mod = i % n
            if (i + (n - 1) - mod >= 0) unit(mod) else nonNegativeLessThan(n)
        }

}
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
        rng => {
            val (a, rng2) = s(rng)
            (f(a), rng2)
        }

    def nonNegativeInt(rng: RNG): (Int, RNG) = {
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
        val (b, rng2) = double(rng)
        val (c, rng3) = double(rng)
        ((a, b, c), rng3)
    }

    def ints(count: Int)(rng: RNG): (List[Int], RNG) = {
        @tailrec
        def go(n: Int, acc: List[Int], rng: RNG): (List[Int], RNG) = {
            if (n <= 0) {
                (acc, rng)
            } else {
                val (a, r) = rng.nextInt
                go(n - 1, a :: acc, r)
            }
        }
        go(count, List(), rng)
    }

    def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] =
        rng => {
            val (a, rng1) = ra(rng)
            val (b, rng2) = rb(rng1)
            (f(a, b), rng2)
        }
}
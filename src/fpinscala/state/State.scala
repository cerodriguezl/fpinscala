package fpinscala.state

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

    def nonNegativeInt(rng: RNG): (Int, RNG) = {
        val (n, r) = rng.nextInt
        (if (n < 0) -(n + 1) else n, r)
    }

    def double(rng: RNG): (Double, RNG) = {
        val (n, r) = nonNegativeInt(rng)
        (n / (Int.MaxValue.toDouble + 1), r)
    }

    def intDouble(rng: RNG): ((Int, Double), RNG) = {
        val (i, rng1) = rng.nextInt
        val (d, rng2) = double(rng1)
        ((i, d), rng2)
    }

}
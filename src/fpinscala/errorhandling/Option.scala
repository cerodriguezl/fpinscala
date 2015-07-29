package fpinscala.errorhandling

import scala.{ Option => _, Some => _, Either => _, _ }

sealed trait Option[+A] {

    def map[B](f: A => B): Option[B] = this match {
        case None        => None
        case Some(value) => Some(f(value))
    }

    def getOrElse[B >: A](default: => B): B = this match {
        case None        => default
        case Some(value) => value
    }

    def flatMap[B](f: A => Option[B]): Option[B] = map(f) getOrElse None

    def orElse[B >: A](ob: => Option[B]): Option[B] = map(Some(_)) getOrElse ob

    def filter(f: A => Boolean): Option[A] = this match {
        case Some(value) if (f(value)) => this
        case _                         => None
    }

    def filterViaFlatMap(f: A => Boolean): Option[A] =
        flatMap(value => if (f(value)) Some(value) else None)

}

case class Some[+A](get: A) extends Option[A]
case object None extends Option[Nothing]

object Option {

    def mean(xs: Seq[Double]): Option[Double] =
        if (xs.isEmpty) None
        else Some(xs.sum / xs.length)

    def variance(xs: Seq[Double]): Option[Double] =
        mean(xs) flatMap (m => mean(xs.map(x => math.pow(x - m, 2))))

    def map2ViaPatternMatching[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = (a, b) match {
        case (None, _)          => None
        case (_, None)          => None
        case (Some(x), Some(y)) => Some(f(x, y))
    }

    def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
        a flatMap (aa => b map (bb => f(aa, bb)))

    def sequence[A](a: List[Option[A]]): Option[List[A]] = a match {
        case Nil    => Some(Nil)
        case h :: t => h flatMap (hh => sequence(t) map (hh :: _))
    }

}
package fpinscala.errorhandling

import scala.{ Option => _, Either => _, Left => _, Right => _, _ }

sealed trait Either[+E, +A] {

    def map[B](f: A => B): Either[E, B] = this match {
        case Left(e)  => Left(e)
        case Right(a) => Right(f(a))
    }

    def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] = this match {
        case Left(e)  => Left(e)
        case Right(a) => f(a)
    }

    def orElse[EE >: E, B >: A](b: => Either[EE, B]): Either[EE, B] = this match {
        case Right(a) => Right(a)
        case Left(_)  => b
    }

    def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] = sys.error("todo")

}

case class Left[+E](get: E) extends Either[E, Nothing]
case class Right[+A](get: A) extends Either[Nothing, A]

object Either {

    def traverse[E, A, B](es: List[A])(f: A => Either[E, B]): Either[E, List[B]] = sys.error("todo")

    def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] = sys.error("todo")

}
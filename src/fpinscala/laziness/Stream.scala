package fpinscala.laziness

import scala.annotation.tailrec
import Stream._

sealed trait Stream[+A] {

    def foldRight[B](z: => B)(f: (A, => B) => B): B =
        this match {
            case Cons(h, t) => f(h(), t().foldRight(z)(f))
            case _          => z
        }

    def toList: List[A] = {
        @tailrec
        def go(s: Stream[A], acc: List[A]): List[A] = s match {
            case Cons(h, t) => go(t(), h() :: acc)
            case _          => acc
        }
        go(this, List()).reverse
    }

    def take(n: Int): Stream[A] = this match {
        case Cons(h, t) if n > 0 => Cons(h, () => t().take(n - 1))
        case _                   => Empty
    }

    @tailrec
    final def drop(n: Int): Stream[A] = this match {
        case Cons(_, t) if n > 0 => t().drop(n - 1)
        case _                   => this
    }

    def takeWhile(p: A => Boolean): Stream[A] =
        foldRight(empty[A])((h, acc) => if (p(h)) cons(h, acc) else empty)

    def forAll(p: A => Boolean): Boolean = foldRight(true)((a, b) => p(a) && b)

    def map[B](p: A => B): Stream[B] = foldRight(empty[B])((h, acc) => cons(p(h), acc))

    def filter(p: A => Boolean): Stream[A] = foldRight(empty[A])((h, acc) => if (p(h)) cons(h, acc) else acc)

    def append[B >: A](s: => Stream[B]): Stream[B] = foldRight(s)((h, acc) => cons(h, acc))

    def flatMap[B](f: A => Stream[B]): Stream[B] = foldRight(empty[B])((h, acc) => f(h) append acc)

}

case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {

    def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
        lazy val head = hd
        lazy val tail = tl
        Cons(() => head, () => tail)
    }

    def empty[A]: Stream[A] = Empty

    def apply[A](as: A*): Stream[A] =
        if (as.isEmpty) empty
        else cons(as.head, apply(as.tail: _*))

    def constant[A](a: A): Stream[A] = {
        lazy val tail: Stream[A] = Cons(() => a, () => tail)
        tail
    }
}
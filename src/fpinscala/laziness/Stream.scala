package fpinscala.laziness

import scala.annotation.tailrec

sealed trait Stream[+A] {

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

}
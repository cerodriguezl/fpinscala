package fpinscala.datastructures

import scala.annotation.tailrec

sealed trait List[+A] // `List` data type, parameterized on a type, `A`
case object Nil extends List[Nothing] // A `List` data constructor representing the empty list
/* Another data constructor, representing nonempty lists. Note that `tail` is another `List[A]`,
which may be `Nil` or another `Cons`.
 */
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List { // `List` companion object. Contains functions for creating and working with lists.
    def sum(ints: List[Int]): Int = ints match { // A function that uses pattern matching to add up a list of integers
        case Nil         => 0 // The sum of the empty list is 0.
        case Cons(x, xs) => x + sum(xs) // The sum of a list starting with `x` is `x` plus the sum of the rest of the list.
    }

    def product(ds: List[Double]): Double = ds match {
        case Nil          => 1.0
        case Cons(0.0, _) => 0.0
        case Cons(x, xs)  => x * product(xs)
    }

    def apply[A](as: A*): List[A] = // Variadic function syntax
        if (as.isEmpty) Nil
        else Cons(as.head, apply(as.tail: _*))

    val x = List(1, 2, 3, 4, 5) match {
        case Cons(x, Cons(2, Cons(4, _)))          => x
        case Nil                                   => 42
        case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
        case Cons(h, t)                            => h + sum(t)
        case _                                     => 101
    }

    def append[A](l: List[A], r: List[A]): List[A] =
        foldRight(l, r)(Cons(_, _))

    def foldRight[A, B](as: List[A], z: B)(f: (A, B) => B): B = // Utility functions
        as match {
            case Nil         => z
            case Cons(x, xs) => f(x, foldRight(xs, z)(f))
        }

    def sum2(ns: List[Int]) =
        foldRight(ns, 0)((x, y) => x + y)

    def product2(ns: List[Double]) =
        foldRight(ns, 1.0)(_ * _) // `_ * _` is more concise notation for `(x,y) => x * y`; see sidebar

    def tail[A](l: List[A]): List[A] = l match {
        case Nil        => sys.error("tail on empty list")
        case Cons(h, t) => t
    }

    def setHead[A](l: List[A], h: A): List[A] = l match {
        case Nil        => sys.error("setHead on empty list")
        case Cons(_, t) => Cons(h, t)
    }

    def drop[A](l: List[A], n: Int): List[A] = {
        if (n <= 0) l
        else l match {
            case Cons(h, t) => drop(t, n - 1)
            case Nil        => Nil
        }
    }

    def dropWhile[A](l: List[A], f: A => Boolean): List[A] = l match {
        case Cons(h, t) if f(h) => dropWhile(t, f)
        case _                  => l
    }

    def init[A](l: List[A]): List[A] = l match {
        case Nil          => sys.error("init on empty list");
        case Cons(_, Nil) => Nil
        case Cons(h, t)   => Cons(h, init(t))
    }

    def length[A](l: List[A]): Int = foldRight(l, 0)((_, acc) => acc + 1)

    @tailrec
    def foldLeft[A, B](l: List[A], z: B)(f: (B, A) => B): B = l match {
        case Nil        => z
        case Cons(h, t) => foldLeft(t, f(z, h))(f)
    }

    def reverse[A](l: List[A]): List[A] = foldLeft(l, List[A]()) { (acc, t) => Cons(t, acc) }

    def concat[A](l: List[List[A]]): List[A] = foldLeft(l, List[A]())(append)

    def map[A, B](l: List[A])(f: A => B): List[B] =
        foldRight(l, Nil: List[B])((h, acc) => Cons(f(h), acc))

    def addOne(l: List[Int]): List[Int] = map(l)(_ + 1)

    def filter[A](l: List[A])(f: A => Boolean): List[A] =
        foldRight(l, Nil: List[A])((h, acc) => if (f(h)) Cons(h, acc) else acc)

    def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] = concat(map(as)(f))

    def filterViaFlatMap[A](l: List[A])(f: A => Boolean): List[A] =
        flatMap(l)(x => if (f(x)) List(x) else Nil)

    def zipWith[A, B, C](a: List[A], b: List[B])(f: (A, B) => C): List[C] = (a, b) match {
        case (Nil, _)                     => Nil
        case (_, Nil)                     => Nil
        case (Cons(ha, ta), Cons(hb, tb)) => Cons(f(ha, hb), zipWith(ta, tb)(f))
    }

}
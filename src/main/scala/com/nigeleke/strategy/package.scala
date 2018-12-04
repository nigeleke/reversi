package com.nigeleke

package object strategy {

  implicit class Scala213Ops[T](values: Seq[T]) {

    def maxByOption[U](fn: T => U)(implicit cmp: Ordering[U]) =
      if (values.isEmpty) None
      else Some(values.maxBy(fn))

    def minByOption[U](fn: T => U)(implicit cmp: Ordering[U]) =
      if (values.isEmpty) None
      else Some(values.minBy(fn))

  }

}

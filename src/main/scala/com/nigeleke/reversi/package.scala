package com.nigeleke

package object reversi {

  implicit class Scala213Ops[T](values: Seq[T]) {

//    def maxByOption[U](fn: T => U)(implicit cmp: Ordering[U]) =
//      if (values.isEmpty) None
//      else Some(values.maxBy(fn))
//
//    def minByOption[U](fn: T => U)(implicit cmp: Ordering[U]) =
//      if (values.isEmpty) None
//      else Some(values.minBy(fn))


    def allMins[U](fn: T => U)(implicit cmp: Ordering[U]) = {
      val groups = values.groupBy(fn)
      val sortedKeys = groups.keySet.toSeq.sorted
      sortedKeys.headOption.map(groups(_)).getOrElse(Seq.empty)
    }
  }

}

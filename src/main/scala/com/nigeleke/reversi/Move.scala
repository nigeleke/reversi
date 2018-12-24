package com.nigeleke.reversi

import scala.language.implicitConversions

sealed trait Move
case class Square(row: Int, column: Int) extends Move {
  override lazy val toString = ((column + 'a').toChar + (row + 1).toString)

}
case object Pass extends Move

object Square {

  implicit def liftImplicitTuple2(tuple: (Int, Int)) = Square(tuple._1, tuple._2)

  implicit class SquareOps(lhs: Square) {
    def +(rhs: Square) = Square(lhs.row + rhs.row, lhs.column + rhs.column)
  }

}

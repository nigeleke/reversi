package com.nigeleke.reversi

import Player._

import scala.annotation.tailrec

// Array doesn't allow deep copy so provide all methods normally provided by case.
class Board(val value: Array[Array[Counter]]) {

  override def equals(other: Any): Boolean = other match {
    case that: Board => (that canEqual this) && (value.deep == that.value.deep)
    case _ => false
  }

  private def canEqual(other: Any): Boolean = other.isInstanceOf[Board]

  override def hashCode(): Int = value.hashCode()

  override def toString = value.map {
    _.map { _.toChar }.mkString(" ")
  }.mkString("\n")

  def toPrettyString = "    a b c d e f g h\n" + value.map {
    _.map { _.toChar }.mkString(" ")
  }.zipWithIndex.map(si => s" ${si._2 + 1}  ${si._1}").mkString("\n")

  def copy(value: Array[Array[Counter]] = value) : Board = new Board(value)
}

object Board {

  private lazy val nRows = 8
  private lazy val nCols = 8

  def apply() : Board = centralSquares
    .foldLeft(new Board(Array.fill(nRows, nCols)(Empty)))((b, s) =>  {
      val p = if (s.row == s.column) Black else White
      b.set(Full(p), s)
    })

  def from(value: String) : Board = {
    val rawValue = value.filter(".OX".contains(_))
    require(rawValue.size == nRows * nCols)
    val rawValueIter = rawValue.iterator
    squares.foldLeft(Board())((b, rc) => {
      import Counter._
      val counter : Counter = rawValueIter.next()
      b.set(counter, rc)
    })
  }

  lazy val squares : Seq[Square] = for (row <- 0 until nRows; col <- 0 until nCols) yield Square(row, col)

  lazy val directions = Seq(
    (-1, -1), (-1,  0), (-1, +1),
    ( 0, -1),           ( 0, +1),
    (+1, -1), (+1,  0), (+1, +1))

  lazy val centralSquares = Seq(Square(3,3), Square(3,4), Square(4,3), Square(4,4))

  implicit class BoardOps(board: Board) {

    def apply(rc: Square) : Counter = board.value(rc.row)(rc.column)

    def availableMoves(who: Player) = {
      if (emptySquares.size > 60) centralSquares intersect emptySquares
      else emptySquares.filter(s => captures(who, s).size > 0)
    }

    lazy val (emptySquares, occupiedSquares) = squares.partition(s => board(s) == Empty)

    private def captures(who: Player, from: Square) : Seq[Square] = for {
      d <- directions
      cs <- captures(who, from, d)
    } yield cs

    private def captures(who: Player, from: Square, direction: Square) : Seq[Square] = {
      val squares = from in direction
      val counters = squares.map(board(_))

      val opponentSquares = squares
        .drop(1) // the 'from' result
        .takeWhile(s => board(s) == Full(who.opponent))
      val optEndSquare = squares.drop(opponentSquares.size + 1).headOption

      optEndSquare.filter(s => board(s) == Full(who)).map(_ => opponentSquares).getOrElse(Seq.empty)
    }

    def makeMove(who: Player, move: Square) = {
      require(availableMoves(who).contains(move))
      captures(who, move)
        .foldLeft(board)((b, rc) => b.set(who, rc))
        .set(who, move)
    }

    private def set(as: Player, square: Square) : Board = set(Full(as), square)

    def set(as: Counter, square: Square) : Board = {
      val value = board.value
      val row = square.row
      val col = square.column
      val updated = value.updated(row, value(row).updated(col, as))
      board.copy(value = updated)
    }

    def counters(who: Player) = (if (who == Black) blackSquares else whiteSquares).size

    lazy val (blackSquares, whiteSquares) = occupiedSquares.partition(s => board(s) == Full(Black))
  }

  private implicit class RowColumnExtensionOps(lhs: Square) {

    def in(direction: Square) : Seq[Square] = line(lhs, direction, Seq.empty[Square])

    @tailrec
    private def line(from: Square, direction: Square, l: Seq[Square]) : Seq[Square] = {
      import Square._
      if (!from.inBounds) l
      else line(from + direction, direction, l :+ from)
    }

    val inBounds = (0 until nRows).contains(lhs.row) && (0 until nCols).contains(lhs.column)

  }
}

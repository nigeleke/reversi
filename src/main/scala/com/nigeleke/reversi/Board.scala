package com.nigeleke.reversi

case class Board(rep: String)

object Board {

  lazy val empty = Board("........\n" * 8)

  implicit class BoardOps(b: Board) {
    lazy val toSimpleString = b.rep
  }

}
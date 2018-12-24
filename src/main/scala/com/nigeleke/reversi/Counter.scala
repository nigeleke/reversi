package com.nigeleke.reversi

import scala.language.implicitConversions

sealed trait Counter
case object Empty extends Counter
case class Full(player: Player) extends Counter

object Counter {

  implicit class CounterOps(counter: Counter) {
    lazy val toChar = counter match {
      case Empty => '.'
      case Full(Black) => 'O'
      case Full(White) => 'X'
    }
  }

  implicit def fromChar(c: Char) = c match {
    case '.' => Empty
    case 'O' => Full(Black)
    case 'X' => Full(White)
  }

}
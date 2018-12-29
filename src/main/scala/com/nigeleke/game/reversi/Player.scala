package com.nigeleke.game.reversi

sealed trait Player
case object Black extends Player
case object White extends Player

object Player {

  implicit class PlayerOps(who: Player) {
    lazy val opponent = who match {
      case Black => White
      case White => Black
    }
  }
}
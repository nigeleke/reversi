package com.nigeleke.game

trait Strategy {

  type Move
  type Game

  def getMove(game: Game) : Option[Move]

}

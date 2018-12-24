package com.nigeleke.reversi

trait Strategy {

  type Game = com.nigeleke.reversi.Reversi
  type Move = com.nigeleke.reversi.Move

  def getMove(game: Game) : Move

}

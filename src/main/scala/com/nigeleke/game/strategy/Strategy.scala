package com.nigeleke.game.strategy

// Acknowledgement:
// This code is loosely derived from the work completed by Phillip Johnson.
// https://github.com/phillipjohnson/dots-and-boxes/
//
trait Strategy {

  type Game <: GameLike
  type Move = Game#Move

  def getMove(game: Game) : Move

}

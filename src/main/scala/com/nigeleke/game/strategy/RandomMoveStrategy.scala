package com.nigeleke.game.strategy

// Acknowledgement:
// This code is loosely derived from the work completed by Phillip Johnson.
// https://github.com/phillipjohnson/dots-and-boxes/
//
trait RandomMoveStrategy extends Strategy {

  override def getMove(game: Game): Move = {
    val moves = game.availableMoves
    val randomMove = scala.util.Random.shuffle(moves).head
    randomMove
  }

}

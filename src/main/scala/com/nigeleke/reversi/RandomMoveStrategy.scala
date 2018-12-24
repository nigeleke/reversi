package com.nigeleke.reversi

// Acknowledgement:
// This code is derived as a result of the work completed by Phillip Johnson.
// https://github.com/phillipjohnson/dots-and-boxes/
//
object RandomMoveStrategy extends Strategy {

  override def getMove(game: Game): Move = {
    val moves = game.availableMoves
    val randomMove = scala.util.Random.shuffle(moves).head
    randomMove
  }

}

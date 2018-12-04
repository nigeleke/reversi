package com.nigeleke.strategy

import com.nigeleke.game.Strategy

// Acknowledgement:
// This code is derived as a result of the work completed by Phillip Johnson.
// https://github.com/phillipjohnson/dots-and-boxes/
//
object Random extends Strategy {

  trait Game {
    def availableMoves : Seq[Move]
  }

  override def getMove(game: Game): Option[Move] = {
    val moves = game.availableMoves
    val optRandomMove = scala.util.Random.shuffle(moves).headOption
    optRandomMove
  }

}

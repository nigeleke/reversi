package com.nigeleke.game.strategy

import com.typesafe.config.{Config, ConfigFactory}

// Acknowledgement:
// This code is loosely derived from the work completed by Phillip Johnson.
// https://github.com/phillipjohnson/dots-and-boxes/
//
trait MiniMax extends Strategy {

  val config: Config
  private lazy val maxDepth = config.getInt("com.nigeleke.game.strategy.MiniMax.maxDepth")

  override def getMove(game: Game): Move = {
    val results = game.availableMoves.map(m => (m, lookahead(game.makeMove(m), 0)))
    val result = results.minBy(_._2)
    result._1
  }

  private def lookahead(game: GameLike, depth: Int) : Int = {
    if (depth == maxDepth || game.isFinished) game.valuation
    else -game.availableMoves.map(m => lookahead(game.makeMove(m), depth + 1)).min

  }

}

package com.nigeleke.reversi

import com.typesafe.config.ConfigFactory

object MiniMax extends Strategy {

  val config = ConfigFactory.load()
  val maxDepth = config.getInt("com.nigeleke.reversi.strategy.MiniMax.maxDepth")

  override def getMove(game: Game): Move = {
    val results = game.availableMoves.map(m => (m, lookahead(game.makeMove(m), 0)))
    val result = results.minBy(_._2)
    result._1
  }

  private def lookahead(game: Game, depth: Int) : Int =
    if (depth == maxDepth || game.isFinished) game.valuation
    else -game.availableMoves.map(m => lookahead(game.makeMove(m), depth + 1)).min

}

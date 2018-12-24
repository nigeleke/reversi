package com.nigeleke.reversi

import com.typesafe.config.ConfigFactory

object MiniMax extends Strategy {

  val config = ConfigFactory.load()
  val trace = config.getBoolean("com.nigeleke.reversi.trace")
  val maxDepth = config.getInt("com.nigeleke.reversi.strategy.MiniMax.maxDepth")

  override def getMove(game: Game): Move = lookahead(game, 0).move

  case class LookAheadResult(move: Move, value: Int)

  private def lookahead(game: Game, depth: Int) : LookAheadResult = {
    val moves = game.availableMoves

    val results =
      if (depth == maxDepth) moves.map(m => LookAheadResult(m, game.makeMove(m).valuation))
      else moves.map(m => LookAheadResult(m, lookahead(game.makeMove(m), depth+1).value))

    val result = results.minBy(_.value)

    LookAheadResult(result.move, -result.value)
  }
}

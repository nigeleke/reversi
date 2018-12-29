package com.nigeleke.game.strategy

import com.typesafe.config.ConfigFactory

// Acknowledgement:
// This code is loosely derived from the work completed by Phillip Johnson.
// https://github.com/phillipjohnson/dots-and-boxes/
//
trait MiniMaxWithAlphaBetaPruning extends Strategy {

  val config = ConfigFactory.load()
  val maxDepth = config.getInt("com.nigeleke.game.strategy.MiniMaxWithAlphaBetaPruning.maxDepth")

  override def getMove(game: Game): Move = {
    val results = game.availableMoves.map(m => (m, lookahead(game.makeMove(m), 0, MinInt, MaxInt)))
    val result = results.minBy(_._2)
    result._1
  }

  private def lookahead(game: GameLike, depth: Int, alpha: Int, beta: Int) : Int =
    if (depth == maxDepth || game.isFinished) game.valuation
    else -prune(game, depth, alpha, beta)

  private def prune(game: GameLike, depth: Int, alpha: Int, beta: Int) : Int =
    game.availableMoves.foldLeft((beta, MaxInt)) {
      case ((currentBeta, bestValue), m) =>
        if (currentBeta <= alpha) (currentBeta, bestValue)
        else {
          val nextMoveValue = lookahead(game.makeMove(m), depth + 1, -currentBeta, -alpha)
          val newBestValue = Math.min(bestValue, nextMoveValue)
          val newBeta = Math.min(beta, newBestValue)
          (newBeta, newBestValue)
        }
    }._2

  private val MaxInt = Int.MaxValue
  private val MinInt = -MaxInt

}

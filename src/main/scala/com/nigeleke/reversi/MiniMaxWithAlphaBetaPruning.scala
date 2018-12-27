package com.nigeleke.reversi

import com.typesafe.config.ConfigFactory

object MiniMaxWithAlphaBetaPruning extends Strategy {

  val config = ConfigFactory.load()
  val maxDepth = config.getInt("com.nigeleke.reversi.strategy.MiniMaxWithAlphaBetaPruning.maxDepth")

  override def getMove(game: Game): Move = lookahead(game, 0, MinInt, MaxInt).move

  case class LookAheadResult(move: Move, value: Int)

  private def lookahead(game: Game, depth: Int, alpha: Int, beta: Int): LookAheadResult = {
    val moves = game.availableMoves

    val result =
      if (depth == maxDepth) moves.map(m => LookAheadResult(m, game.makeMove(m).valuation)).minBy(_.value)
      else alphaBetaResult(game, depth, moves, alpha, beta)

    LookAheadResult(result.move, -result.value)
  }

  private def alphaBetaResult(game: Game, depth: Int, moves: Seq[Move], alpha: Int, beta: Int) : LookAheadResult = {
    val (firstMove, rest) = (moves.head, moves.tail)
    val firstLookahead = lookahead(game.makeMove(firstMove), depth + 1, -beta, -alpha)

    val (bestMove, bestLookahead) =
      rest.foldLeft((firstMove, firstLookahead)) {
        case c @ ((goodM, goodLar), m) =>
          if (goodLar.value <= alpha) (goodM, goodLar)
          else {
            val lar = lookahead(game.makeMove(m), depth + 1, -beta, -goodLar.value)
            if (lar.value < goodLar.value) (m, lar)
            else (goodM, goodLar)
          }
      }

    LookAheadResult(bestMove, bestLookahead.value)
  }

  private val MaxInt = Int.MaxValue
  private val MinInt = -MaxInt

}

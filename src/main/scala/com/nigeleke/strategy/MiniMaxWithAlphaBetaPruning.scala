package com.nigeleke.strategy

import com.nigeleke.game.Strategy

// Acknowledgement:
// This code is heavily based on the work completed by Phillip Johnson.
// https://github.com/phillipjohnson/dots-and-boxes/
// https://letstalkdata.com/2015/01/implementing-minimax-in-scala-naive-minimax/
//
object MiniMaxWithAlphaBetaPruning extends Strategy {

  trait Game {
    def availableMoves : Seq[Move]
    def makeMove(move: Move) : this.type
    def isFinished : Boolean
    def valuation : Int
  }

  override def getMove(game: Game) : Option[Move] = {
    val moveScores = for (move <- game.availableMoves) yield move -> minimax(game.makeMove(move))
    val bestMove = moveScores.minByOption(_._2).headOption.map(_._1)
    bestMove
  }

  private val maxDepth = 3

  private def minimax(game: Game) : Int = minimize(game, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE)

  private def minimize(game: Game, depth: Int, alpha: Int, beta: Int) : Int = {
    if(game.isFinished || depth == 0) return game.valuation
    var newBeta = beta
    game.availableMoves.foreach(move => {
      val newState = game.makeMove(move)
      newBeta = math.min(beta, maximize(newState, depth - 1, alpha, newBeta))
      if (alpha >= newBeta) return alpha
    })
    newBeta
  }

  private def maximize(game: Game, depth: Int, alpha: Int, beta: Int) : Int = {
    if(game.isFinished || depth == 0) return game.valuation
    var newAlpha = alpha
    game.availableMoves.foreach(move => {
      val newState = game.makeMove(move)
      newAlpha = math.max(newAlpha, minimize(newState, depth - 1, newAlpha, beta))
      if (newAlpha >= beta) return beta
    })
    newAlpha
  }

}

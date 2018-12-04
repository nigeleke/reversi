package com.nigeleke.strategy

import com.nigeleke.game.Strategy

// Acknowledgement:
// This code is heavily based on the work completed by Phillip Johnson.
// https://github.com/phillipjohnson/dots-and-boxes/
// https://letstalkdata.com/2015/01/implementing-minimax-in-scala-naive-minimax/
//
object MiniMax extends Strategy {

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

  private def minimax(game: Game) : Int = minimize(game, maxDepth)

  private def minimize(game: Game, depth: Int) : Int = {
    if (game.isFinished || depth == 0) return game.valuation
    var bestResult = Integer.MAX_VALUE
    game.availableMoves.foreach(move => {
      val newgame = game.makeMove(move)
      val bestChildResult = maximize(newgame, depth - 1)
      bestResult = math.min(bestResult, bestChildResult)
    })
    bestResult
  }

  private def maximize(game: Game, depth: Int) : Int = {
    if (game.isFinished || depth == 0) return game.valuation
    var bestResult = Integer.MIN_VALUE
    game.availableMoves.foreach(move => {
      val newgame = game.makeMove(move)
      val bestChildResult = minimize(newgame, depth - 1)
      bestResult = math.max(bestResult, bestChildResult)
    })
    bestResult
  }

}

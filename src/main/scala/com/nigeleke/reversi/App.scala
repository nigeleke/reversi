package com.nigeleke.reversi

import scala.annotation.tailrec

object App {

  def main(args: Array[String]) : Unit = {
    val start = System.nanoTime()

    val game = Reversi()
      .withStrategy(Black, MiniMaxWithAlphaBetaPruning)
      .withStrategy(White, MiniMaxWithAlphaBetaPruning)

    val endGame = play(game)

    val end = System.nanoTime()

    println(s"Result: Black ${endGame.board.counters(Black)} White ${endGame.board.counters(White)} :: total ${(end-start)/1000000000.0}ns")
  }

  @tailrec
  private def play(game: Reversi) : Reversi = {
    println(game.board.toPrettyString)
    if (game.isFinished) game
    else {
      val strategy = game.strategy(game.currentPlayer)
      val move = strategy.getMove(game)
      val newGame = game.makeMove(move)
      play(newGame)
    }
  }

}

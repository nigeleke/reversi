package com.nigeleke.game.reversi

import scala.annotation.tailrec

object App {

  def main(args: Array[String]) : Unit = {
    val start = System.nanoTime()

    val game = Reversi()
      .withStrategy(Black, miniMaxWithAlphaBetaPruning)
      .withStrategy(White, miniMaxWithAlphaBetaPruning)

    val endGame = play(game)

    val end = System.nanoTime()

    println(s"Result: Black ${endGame.board.counters(Black)} White ${endGame.board.counters(White)} in ${(end-start)/1000000000}s")
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

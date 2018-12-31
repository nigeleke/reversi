package com.nigeleke.game.reversi

import com.nigeleke.game.strategy.{MiniMax, MiniMaxWithAlphaBetaPruning, RandomMoveStrategy}
import com.typesafe.config.ConfigFactory

import scala.annotation.tailrec

object App {

  def main(args: Array[String]) : Unit = {
    val defaultConfig = ConfigFactory.load()

    val start = System.nanoTime()

    lazy val randomMoveStrategy = new ReversiStrategy with RandomMoveStrategy {}
    lazy val miniMaxStrategy = new ReversiStrategy with MiniMax { val config = defaultConfig }
    lazy val miniMaxWithAlphaBetaPruning = new ReversiStrategy with MiniMaxWithAlphaBetaPruning { val config = defaultConfig }
    lazy val manualStrategy = ManualStrategy(System.in, System.out)

    val game = Reversi()
      .withStrategy(Black, miniMaxWithAlphaBetaPruning)
      .withStrategy(White, manualStrategy)

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

package com.nigeleke.game.reversi

import java.io._

import com.nigeleke.game.strategy.{MiniMax, MiniMaxWithAlphaBetaPruning, RandomStrategy}
import com.typesafe.config.{Config, ConfigFactory}

import scala.annotation.tailrec

class App(implicit defaultConfig: Config, in: InputStream, out: OutputStream) {

  lazy val reader = new BufferedReader(new InputStreamReader(in))
  lazy val writer = new PrintWriter(out)

  lazy val strategy = Map(
    "random" -> new ReversiStrategy with RandomStrategy {},
    "miniMax" -> new ReversiStrategy with MiniMax { val config = defaultConfig },
    "miniMaxWithAlphaBetaPruning" -> new ReversiStrategy with MiniMaxWithAlphaBetaPruning { val config = defaultConfig },
    "manual" -> ManualStrategy(in, out))

  lazy val blackStrategy = strategy(defaultConfig.getString("com.nigeleke.game.reversi.blackStrategy"))
  lazy val whiteStrategy = strategy(defaultConfig.getString("com.nigeleke.game.reversi.whiteStrategy"))

  def run() = {
    val start = System.nanoTime()

    val initialGame = Reversi()
      .withStrategy(Black, blackStrategy)
      .withStrategy(White, whiteStrategy)

    val endGame = play(initialGame)

    val end = System.nanoTime()

    writer.println(s"Result: Black ${endGame.board.counters(Black)} White ${endGame.board.counters(White)} in ${(end-start)/1000000000}s")
    writer.flush()
  }

  @tailrec
  private def play(game: Reversi) : Reversi = {
    writer.println(game.board.toPrettyString)
    if (game.isFinished) game
    else {
      val strategy = game.strategy(game.currentPlayer)
      val move = strategy.getMove(game)
      val newGame = game.makeMove(move)
      play(newGame)
    }
  }

}

object App {

  def main(args: Array[String]) : Unit = {
    implicit val defaultConfig = ConfigFactory.load()
    implicit val inputStream = System.in
    implicit val outputStream = System.out
    val app = new App()
    app.run()
  }

}

package com.nigeleke.game.reversi

import com.nigeleke.game.strategy.Strategy

trait ManualStrategy extends Strategy {

  override type Game = Reversi
  override type Move = ReversiMove

  override def getMove(game: Game): Move = {
    val validCommandMap = game.availableMoves.map {
      case p @ Pass => "pass" -> p
      case s @ Square(row, column) => s.toString -> s
    }.toMap

    val validCommandsString = validCommandMap.keys.toSeq.sorted.mkString(", ")
    println(s"Allowed: $validCommandsString")

    val command = Iterator
      .continually(scala.io.StdIn.readLine("Move: "))
      .dropWhile(c => !(validCommandMap.contains(c) || c.equals("quit")))
      .next()

    if (command.equals("quit")) System.exit(0)

    validCommandMap(command)
  }

}

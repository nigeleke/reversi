package com.nigeleke.game.reversi

import java.io._

case class ManualStrategy(in: InputStream, out: OutputStream) extends ReversiStrategy {

  lazy val reader = new BufferedReader(new InputStreamReader(in))
  lazy val writer = new PrintWriter(out)

  override def getMove(game: Game): Move = {
    val validCommandMap = game.availableMoves.map {
      case p @ Pass => "pass" -> p
      case s @ Square(row, column) => s.toString -> s
    }.toMap

    val validCommandsString = validCommandMap.keys.toSeq.sorted.mkString(", ")
    writer.println(s"Allowed: $validCommandsString")

    val command = Iterator
      .continually({ writer.print("Move: "); writer.flush(); reader.readLine() })
      .dropWhile(c => !(validCommandMap.contains(c) || c.equals("quit")))
      .next()

    if (command.equals("quit")) System.exit(0)

    validCommandMap(command)
  }

}

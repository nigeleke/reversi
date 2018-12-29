package com.nigeleke.game.reversi

import com.nigeleke.game.strategy.{GameLike, Strategy}

case class Reversi(board: Board, currentPlayer: Player, strategy: Map[Player, ReversiStrategy]) extends GameLike {

  override type Game = Reversi
  override type Move = ReversiMove

  override lazy val availableMoves: Seq[Move] = {
    val boardMoves = board.availableMoves(currentPlayer)
    if (boardMoves.isEmpty) Seq(Pass) else boardMoves
  }

  override def makeMove(move: Move): Game = {
    require(availableMoves.contains(move))
    val newBoard = move match {
      case Pass => board
      case s: Square => board.makeMove(currentPlayer, s)
    }
    copy(currentPlayer = opponent, board = newBoard)
  }

  lazy val opponent: Player = currentPlayer.opponent

  override lazy val isFinished: Boolean = board.availableMoves(currentPlayer).isEmpty && board.availableMoves(opponent).isEmpty

  override lazy val valuation: Int =
    if (isFinished) finishedGameValuation
    else unfinishedGameValuation

  private lazy val finishedGameValuation: Int = {
    val diff = board.counters(currentPlayer) - board.counters(opponent)
    if (diff > 0) Int.MaxValue
    else if (diff < 0) Int.MinValue +1
    else 0
  }

  private lazy val (currentSquares, opponentSquares) = board.occupiedSquares.partition(s => board(s) == Full(currentPlayer))
  private lazy val (currentCounters, opponentCounters) = (currentSquares.size, opponentSquares.size)

  private lazy val unfinishedGameValuation: Int = (currentCounters, opponentCounters) match {
    case (0, 0) => 0
    case (0, _) => -Int.MaxValue
    case (_, 0) => Int.MaxValue
    case (cc, oc) if Reversi.startGame.contains(cc + oc) => maximizeMobility
    case (cc, oc) if Reversi.midGame.contains(cc + oc) => weighted
    case (cc, oc) if Reversi.endGame.contains(cc + oc) => maximizeCounters
    case counts => throw new RuntimeException(s"Reverei::unfinishedGameEvaluation error: counts=$counts")
  }

  private lazy val maximizeMobility = {
    // Maximize number of moves available during start
    val moves = board.availableMoves(currentPlayer)
    moves.map(Reversi.weightOf).sum * moves.size
  }

  private lazy val weighted =
    currentSquares.map(Reversi.weightOf).sum - opponentSquares.map(Reversi.weightOf).sum

  private lazy val maximizeCounters = {
    // End game simply counter counters. (if depth is range size this will be perfect game)
    currentCounters - opponentCounters
  }

}

object Reversi {

  def apply(board: Board, player: Player) : Reversi = Reversi(board, player, Map(Black -> manualStrategy, White -> manualStrategy))
  def apply() : Reversi = Reversi(Board(), Black)

  implicit class GameOps(game: Reversi) {
    def withStrategy(player: Player, strategy: ReversiStrategy) = {
      val newStrategy = game.strategy.updated(player, strategy)
      game.copy(strategy = newStrategy)
    }
  }

  private lazy val startGame = 0 until 10
  private lazy val midGame = (startGame.last + 1) until 55
  private lazy val endGame = (midGame.last + 1) until 64

  lazy val weights : Array[Array[Int]] = Array(
    Array( 99,  -8,  8,  6,  6,  8,  -8, 99 ),
    Array( -8, -24, -4, -3, -3, -4, -24, -8 ),
    Array(  8,  -4,  7,  4,  4,  7,  -4,  8 ),
    Array(  6,  -3,  4,  0,  0,  4,  -3,  6 ),
    Array(  6,  -3,  4,  0,  0,  4,  -3,  6 ),
    Array(  8,  -4,  7,  4,  4,  7,  -4,  8 ),
    Array( -8, -24, -4, -3, -3, -4, -24, -8 ),
    Array( 99,  -8,  8,  6,  6,  8,  -8, 99 ))

  def weightOf(s: Square) = weights(s.row)(s.column)
}

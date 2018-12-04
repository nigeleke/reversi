package com.nigeleke.reversi

//import com.nigeleke.game.{ Game => GenericGame, Move => GenericMove }
//import com.nigeleke.reversi.{ Move => ReversiMove }

case class Reversi(board: Board, currentPlayer: Player)

object Reversi {

  def apply(): Reversi = Reversi(Board.empty, Black)

//  implicit class GameOps(game: Reversi) extends Game[Reversi] {
//  }

}

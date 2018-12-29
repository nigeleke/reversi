package com.nigeleke.game

import com.nigeleke.game.strategy._

package object reversi {

  val randomMoveStrategy = new ReversiStrategy with RandomMoveStrategy {}
  val miniMaxStrategy = new ReversiStrategy with MiniMax {}
  val miniMaxWithAlphaBetaPruning = new ReversiStrategy with MiniMaxWithAlphaBetaPruning {}
  val manualStrategy = new ReversiStrategy with ManualStrategy {}

}

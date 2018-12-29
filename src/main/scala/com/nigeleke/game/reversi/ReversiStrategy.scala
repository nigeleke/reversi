package com.nigeleke.game.reversi

import com.nigeleke.game.strategy.Strategy

trait ReversiStrategy extends Strategy {
  override type Game = Reversi
  override type Move = ReversiMove
}

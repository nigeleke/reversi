package com.nigeleke.game.strategy

trait GameLike {

  type Game <: GameLike
  type Move

  def availableMoves: Seq[Move]
  def makeMove(move: Move) : Game
  def isFinished: Boolean
  def valuation: Int

}

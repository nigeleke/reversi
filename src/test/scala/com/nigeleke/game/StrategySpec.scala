package com.nigeleke.game

import com.nigeleke.game.strategy.{GameLike, MiniMax, MiniMaxWithAlphaBetaPruning, RandomStrategy}
import com.typesafe.config.{Config, ConfigFactory, ConfigValue}
import org.scalatest.{Matchers, WordSpec}

class StrategySpec extends WordSpec with Matchers {

  lazy val testConfig = ConfigFactory.parseString(
    """com.nigeleke.game.strategy.MiniMax.maxDepth = 3
      |com.nigeleke.game.strategy.MiniMaxWithAlphaBetaPruning.maxDepth = 3
      |""".stripMargin)

  "A MiniMax strategy will return the move for the worst position for the opponent" in {
    var move1 =
      moves(
        moves(
          moves(value(10), value(Int.MaxValue)),
          moves(value(5))),
        moves(
          moves(value(-10))))

    val move2 = moves(
      moves(
        moves(value(7), value(5)),
        moves(value(-Int.MaxValue))),
      moves(
        moves(value(-7), value(-5))))

    val game = TestGame(moves(move1, move2))

    val strategy = new MiniMax {
      override val config = testConfig
      override type Game = TestGame
    }

    strategy.getMove(game) should be(move2)
  }

  "A MiniMaxWithAlphaBetaPruning strategy will return the worst position for the opponent" in {
    val move1 = moves(
      moves(
        moves(value(5), value(6)),
        moves(value(7), value(4), value(5))
      ),
      moves(
        moves(value(3))))

    val move2 = moves(
      moves(
        moves(value(6)),
        moves(value(6), value(9))
      ),
      moves(
        moves(value(7))))

    val move3 = moves(
      moves(
        moves(value(5))
      ),
      moves(
        moves(value(9), value(8)),
        moves(value(6))))

    val game = TestGame(moves(move1, move2, move3))

    val strategy = new MiniMaxWithAlphaBetaPruning {
      override val config = testConfig
      override type Game = TestGame
    }

    strategy.getMove(game) should be(move2)
  }

  "A RandomStrategy will return any move" in {
    val availableMoves = moves(value(1), value(2), value(3), value(4))

    val game = TestGame(availableMoves)

    val strategy = new RandomStrategy {
      override type Game = TestGame
    }

    availableMoves.moves should contain (strategy.getMove(game))
  }

  trait TestMove
  case class Branch(moves: Seq[TestMove]) extends TestMove
  case class Leaf(value: Int) extends TestMove

  def moves(ms: TestMove*) = Branch(ms)
  def value(value: Int) = Leaf(value)

  case class TestGame(moves: TestMove, optValuation: Option[Int] = None) extends GameLike {

    override type Game = TestGame
    override type Move = TestMove

    override def availableMoves: Seq[Move] = moves match {
      case b: Branch => b.moves
      case _: Leaf => ??? // Test should not get to this level
    }

    override def makeMove(move: Move): TestGame = move match {
      case b: Branch => TestGame(move, optValuation)
      case l @ Leaf(v) => TestGame(l, Some(v))
    }

    override def isFinished: Boolean = false

    override def valuation: Int = moves match {
      case _: Branch => ??? // Test should not value branches
      case Leaf(v) => v
    }
  }

}

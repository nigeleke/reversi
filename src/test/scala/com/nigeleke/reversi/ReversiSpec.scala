package com.nigeleke.reversi

import com.nigeleke.game.Strategy
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

class ReversiSpec extends WordSpec with MockitoSugar with Matchers {

  "A Reversi Game" should {

    "provide interfaces required by the a game strategy" in {
//      val strategy1 = mock[Strategy]
//      val strategy2 = mock[Strategy]
//      val game = Reversi()
//      game.availableMoves should be(Seq.empty[Move])
////      game.makeMove(Move(3,3)) should be(Reversi(White, ))
//      game.isFinished should be(false)
//      game.valuation should be(0)
      fail()
    }

    "start with an empty board" in {
      val game = Reversi()
      game.board.toSimpleString should be(
        """........
          |........
          |........
          |........
          |........
          |........
          |........
          |........
          |""".stripMargin)
    }

    "start with black as the first player" in {
      val game = Reversi()
      game.currentPlayer should be(Black)
    }

    "find available moves for the current player" ignore { }

    "find no moves when none available" ignore { }

    "Allow a valid move to be made, which" should {

      "flip counters trapped along compass points" in {
        val game = Reversi().withMoves((3,3), (3,4), (4,4), (4,3))
      }

      "not flip other counters" ignore { }

      "change the current player to the other player" ignore { }

    }


  }

  private implicit class ReversiOps(game: Reversi) {
    def withMoves(moveTuples: (Int, Int)*) = {
      val moves = moveTuples.map(t => Move(t._1, t._2))
      // moves.foldLeft(game)((g, m) => g.makeMove(m))
      ???
      game
    }
  }

}

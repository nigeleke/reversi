package com.nigeleke.game

import com.nigeleke.game.reversi.{Black, Board, Square, White}
import org.scalatest.{Matchers, WordSpec}

class BoardSpec extends WordSpec with Matchers {

  "A Board" should {
    "should be created with central squares populated" in {
      val board = Board()
      board.toString should be(
        """. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . O X . . .
          |. . . X O . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin)
    }

    "should be able to be preset" in {
      val initial =
        """. O X . O X . O
          |X . O X . O X .
          |O X . O X . O X
          |. O X . O X . O
          |X . O X . O X .
          |O X . O X . O X
          |. O X . O X . O
          |X . O X . O X .""".stripMargin
      val board = Board.from(initial)
      board.toString should be(initial)
    }

    "should throw IllegalArgument if creating Board.from(invalid characters)" in assertThrows[IllegalArgumentException] {
      val initial =
        """. O X . O X . O
          |D E A D B E E F
          |O X . O X . O X
          |. O X . O X . O
          |X . O X . O X .
          |O X . O X . O X
          |. O X . O X . O
          |X . O X . O X .""".stripMargin
      Board.from(initial)
    }

    "should throw IllegalArgument if creating Board.from(incorrect number of characters)" in assertThrows[IllegalArgumentException] {
      val initial = ". O X . O X . O"
      Board.from(initial)
    }

    "should provide valid moves for Player" in {
      val initial =
        """. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . O X . . .
          |. . . X O . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin
      val board = Board.from(initial)

      board.availableMoves(Black) should contain theSameElementsAs(Seq(Square(2,4), Square(3,5), Square(4,2), Square(5,3)))
    }

    "should, if created from empty, only allow central squares for the moves" in {
      val initial =
        """. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin
      val board = Board.from(initial)

      board.availableMoves(Black) should contain theSameElementsAs(Seq(Square(3,3), Square(3,4), Square(4,3), Square(4,4)))
    }

    "should provide updated board after result" in {
      val initial =
        """. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . O X . . .
          |. . . X O . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin
      val board = Board.from(initial)

      board.makeMove(Black, (2,4)).toString should be(
        """. . . . . . . .
          |. . . . . . . .
          |. . . . O . . .
          |. . . O O . . .
          |. . . X O . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin)
    }

    "should not allow an invalid result to be made" in {
      val board = Board.from(
        """. . . . . . . .
          |. . . . . . . .
          |. . . . O . . .
          |. . . O O . . .
          |. . . X O . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin)
      assertThrows[IllegalArgumentException] {
        board.makeMove(White, Square(4,2))
      }
    }

    "should provide pretty version for printing" in {
      val initial =
        """. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . O X . . .
          |. . . X O . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin
      val board = Board.from(initial)
      board.toPrettyString should be(
        """    a b c d e f g h
          | 1  . . . . . . . .
          | 2  . . . . . . . .
          | 3  . . . . . . . .
          | 4  . . . O X . . .
          | 5  . . . X O . . .
          | 6  . . . . . . . .
          | 7  . . . . . . . .
          | 8  . . . . . . . .""".stripMargin)
    }
  }

  "defect 001: h3 should be valid result for White" in {
    val initial =
      """. . . . . . . X
        |. . . . . O O O
        |. . . X O X . .
        |. . . O X . . .
        |. . O O O X . .
        |. . . . . . . .
        |. . . . . . . .
        |. . . . . . . .""".stripMargin
    val board = Board.from(initial)
    board.availableMoves(White) should contain(Square(2,7))
  }

}

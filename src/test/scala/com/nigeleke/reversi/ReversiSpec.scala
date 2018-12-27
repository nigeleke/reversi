package com.nigeleke.reversi

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

class ReversiSpec extends WordSpec with MockitoSugar with Matchers {

  "A Reversi Game" should {

    "should provide interfaces required by the game strategy" in {
      val game = Reversi()
      game.availableMoves should contain theSameElementsAs(Seq(Square(2,4), Square(3,5), Square(4,2), Square(5,3)))
      val newGame = game.makeMove(Square(2,4))
      newGame.board should be(Board.from(
        """. . . . . . . .
          |. . . . . . . .
          |. . . . O . . .
          |. . . O O . . .
          |. . . X O . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin))
      newGame.currentPlayer should be(White)
      newGame.isFinished should be(false)
      newGame.valuation should (be(a[Int]) or be(a[Integer]))
    }

    "should start with a board with central squares occupied" in {
      val game = Reversi()
      game.board.toString should be(
        """. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . O X . . .
          |. . . X O . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin)
    }

    "should start with black as the firstMove player" in {
      val game = Reversi()
      game.currentPlayer should be(Black)
    }

    "should find available moves for the current player" in {
      val game = Reversi()
      game.availableMoves should contain theSameElementsAs(Seq(Square(2,4), Square(3,5), Square(4,2), Square(5,3)))
    }

    "should find no moves when none available" in {
      val board = Board.from(
        """O O O X . . . .
          |O O X . . . . .
          |O X . . . . . .
          |X . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin)
      val game = Reversi(board, White)
      game.availableMoves should contain theSameElementsAs(Seq(Pass))
    }

    "should allow a valid result to be made" in {
      val game = Reversi()

      val gameAfterMove = game.makeMove(Square(2,4))
      gameAfterMove.board.toString should be(
        """. . . . . . . .
          |. . . . . . . .
          |. . . . O . . .
          |. . . O O . . .
          |. . . X O . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin)

      gameAfterMove.currentPlayer should be(game.opponent)
    }

    "should not allow an invalid result to be made" in {
      val game = Reversi()

      assertThrows[IllegalArgumentException] {
        game.makeMove(Square(2,3))
      }
    }

    "should declare game finished when there are no moves available for either player" in {
      val board = Board.from(
        """. . . . . . . .
          |. . . . . . . .
          |. . . . . O . .
          |. . . O O O . .
          |. . O O O . . .
          |. . O . . . . .
          |. . . . . . . .
          |. . . . . . . .""".stripMargin)

      val blackGame = Reversi(board, Black)
      blackGame.isFinished should be(true)

      val whiteGame = Reversi(board, White)
      whiteGame.isFinished should be(true)
    }

    "should value a finished game at +/- infinity for the winner/loser" in {
      val boardLayouts = Seq(
        """. . . . . . . .
          |. . . . . . . .
          |. . . . . O . .
          |. . . O O O . .
          |. . O O O . . .
          |. . O . . . . .
          |. . . . . . . .
          |. . . . . . . .""",
        """O O O O O O O O
          |O O O O O O O O
          |O O O O O O O O
          |O O O O O O O O
          |O X X X X X X X
          |X X X X X X X X
          |X X X X X X X X
          |X X X X X X X X""")

      val boards = boardLayouts.map(s => Board.from(s.stripMargin))

      boards.foreach { board =>
        val blackGame = Reversi(board, Black)
        blackGame.valuation should be(Int.MaxValue)

        val whiteGame = Reversi(board, White)
        whiteGame.valuation should be(-Int.MaxValue)
      }
    }

    "should value a tied finished game at zero for both players" in {
      val boardLayouts = Seq(
        """O O O . . . . .
          |O O . . . . . .
          |O . . . . . . .
          |. . . . . . . .
          |. . . . . . . .
          |. . . . . . . X
          |. . . . . . X X
          |. . . . . X X X""",
        """O O O O O O O O
          |O O O O O O O O
          |O O O O O O O O
          |O O O O O O O O
          |X X X X X X X X
          |X X X X X X X X
          |X X X X X X X X
          |X X X X X X X X""")

      val boards = boardLayouts.map(s => Board.from(s.stripMargin))

      boards.foreach { board =>
        val blackGame = Reversi(board, Black)
        blackGame.valuation should be(0)

        val whiteGame = Reversi(board, White)
        whiteGame.valuation should be(0)
      }
    }

    "should value an a board for White the opposite to that for Black" in {
      val board = Board.from(
        """. O X . O X . O
          |X . O X . O X .
          |O X . O X . O X
          |. O X . O X . O
          |X . O X . O X .
          |O X . O X . O X
          |. O X . O X . O
          |X X O X X O X X
        """.stripMargin
      )
      val blackGame = Reversi(board, Black)
      val whiteGame = Reversi(board, White)

      blackGame.valuation should be(-whiteGame.valuation)
    }

    "should have default Manual strategies for both players" in {
      val game = Reversi()
      game.strategy(Black) should be(ManualStrategy)
      game.strategy(White) should be(ManualStrategy)
    }

    "should be able to define strategies for both players" in {
      val game = Reversi().withStrategy(Black, MiniMax).withStrategy(White, RandomMoveStrategy)
      game.strategy(Black) should be(MiniMax)
      game.strategy(White) should be(RandomMoveStrategy)
    }
  }

  private implicit class ReversiOps(game: Reversi) {
    def withMoves(moveTuples: (Int, Int)*) = {
      val moves = moveTuples.map(t => Square(t._1, t._2))
      moves.foldLeft(game)((g, m) => g.makeMove(m))
    }
  }

}

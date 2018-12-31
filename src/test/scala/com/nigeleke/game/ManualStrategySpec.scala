package com.nigeleke.game

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.security.Permission

import com.nigeleke.game.reversi.{Board, ManualStrategy, Reversi, White}
import org.scalatest.{Matchers, WordSpec}

class ManualStrategySpec extends WordSpec with Matchers {

  "A ManualStrategy" should {

    "should display availableMoves to a user, reject invalid input and accept a valid input" in {
      val inStream = new ByteArrayInputStream("a1\nc5\n".getBytes)
      val outStream = new ByteArrayOutputStream()
      val game = Reversi()
      val strategy = ManualStrategy(inStream, outStream)
      val move = strategy.getMove(game)

      outStream.toString should be("Allowed: c5, d6, e3, f4\nMove: Move: ")
      move.toString should be("c5")
    }

    "should force user to pass when that's the only available move" in {
      val inStream = new ByteArrayInputStream("pass\n".getBytes)
      val outStream = new ByteArrayOutputStream()
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
      val strategy = ManualStrategy(inStream, outStream)
      val move = strategy.getMove(game)

      outStream.toString should be("Allowed: pass\nMove: ")
      move.toString should be("Pass")
    }

    "should allow a user to quit" in {
      val prevSecurityManager = System.getSecurityManager()

      System.setSecurityManager(new CheckExitSecurityManager())

      val inStream = new ByteArrayInputStream("quit\n".getBytes)
      val outStream = new ByteArrayOutputStream()
      val game = Reversi()
      val strategy = ManualStrategy(inStream, outStream)

      val exception = intercept[ExitException] {
        val move = strategy.getMove(game)
      }

      outStream.toString should be("Allowed: c5, d6, e3, f4\nMove: ")
      exception.status should be(0)

      System.setSecurityManager(prevSecurityManager)
    }

  }

  sealed case class ExitException(status: Int) extends SecurityException("Expected System.exit(0) called") { }

  sealed class CheckExitSecurityManager extends SecurityManager {
    override def checkPermission(perm: Permission): Unit = {}

    override def checkPermission(perm: Permission, context: Object): Unit = {}

    override def checkExit(status: Int): Unit = {
      super.checkExit(status)
      throw ExitException(status)
    }
  }

}


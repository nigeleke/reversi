package com.nigeleke.game

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.nigeleke.game.reversi._
import com.nigeleke.game.strategy.RandomStrategy
import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpec}

class ReversiAppSpec extends WordSpec with Matchers {

  implicit lazy val testConfig = ConfigFactory.parseString(
    """com.nigeleke.game.reversi.blackStrategy = "random"
      |com.nigeleke.game.reversi.whiteStrategy = "random"
      |""".stripMargin)

  "The ReversiApp" should {

    "should run to completion with strategies derived from config" in {
      implicit val inStream = new ByteArrayInputStream("".getBytes)
      implicit val outStream = new ByteArrayOutputStream()
      val app = new App()
      app.run()

      outStream.toString should startWith("    " +
        """a b c d e f g h
           | 1  . . . . . . . .
           | 2  . . . . . . . .
           | 3  . . . . . . . .
           | 4  . . . O X . . .
           | 5  . . . X O . . .
           | 6  . . . . . . . .
           | 7  . . . . . . . .
           | 8  . . . . . . . .
           |""".stripMargin)
    }

  }


}

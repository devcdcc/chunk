package com.github.devcdcc.foop
package parser.services

import fastparse._, NoWhitespace._
import zio.*
import zio.test.*

object LiteralReaderSpec extends ZIOSpecDefault {
  private inline def ValidLiterals   =
    List("2332", "12", "True", "False", "12.12341234", "\"asfdasd\"", "-1", "-1.2123423")
  private inline def InvalidLiterals =
    List("a", "abcdf", "aasdfas0", "0.0.0", "'asss", "true", "false", " .", " ", ":", "!")
  import Assertion.*
  override def spec                  = suite("LiteralReader.literal")(
    test("succeed with valid literals") {
      // given
      val identifier = ValidLiterals
      for {
        // when
        response <- ZIO.foreach(identifier)(literal => zioFromParsed(parse(literal, LiteralReader.test)).either)
        // then
      } yield assertTrue(response.forall(_.isRight))
    },
    test("fail with invalid literals") {
      // given
      val identifier = InvalidLiterals
      for {
        // when
        response <- ZIO.foreach(identifier)(literal => zioFromParsed(parse(literal, LiteralReader.test)).either)
        // then
      } yield assertTrue(response.forall(_.isLeft))
    }
  )
}

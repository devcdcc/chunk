package com.github.devcdcc.foop
package parser.services

import fastparse._, NoWhitespace._

import zio._
import zio.test.*
import zio.test.Assertion.*

object IdentifierReaderSpec extends ZIOSpecDefault {
  private val subject = new IdentifierReader {}

  override def spec = suite("IdentifierReader.identifier")(
    test("should succeed with valid identifiers") {
      // given
      val identifierLines = """a
                              |bv
                              |abc
                              |$abc
                              |_abc
                              |_01
                              |_00
                              |_abc01
                              |__
                              |aaa_01
                              |asda01_""".stripMargin
      val identifiers     = identifierLines.split("\n")
      for {
        // when
        responses <- ZIO.foreach(identifiers) { identifier =>
          zioFromParsed(parse(identifier, subject.identifier)).either
        }
        // then
      } yield assertTrue(responses.forall(_.isRight))
    },
    test("should fail on invalid identifiers") {
      // given
      val identifierLines = """0
                              |000asdfa
                              |%%
                              |23
                              |@@
                              |!43#""".stripMargin
      val identifiers     = identifierLines.split("\n")
      for {
        // when
        responses <- ZIO.foreach(identifiers) { identifier =>
          zioFromParsed(parse(identifier, subject.identifier)).either
        }
        // then
      } yield assertTrue(responses.forall(_.isLeft))
    },
  )
}

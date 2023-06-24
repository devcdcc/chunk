package com.github.devcdcc.foop
package parser.services

import fastparse.*
import NoWhitespace.*
import com.github.devcdcc.foop.parser.domain.Identifier
import zio.*
import zio.test.*
import zio.test.Assertion.*

object IdentifierReaderSpec extends ZIOSpecDefault {
  private val subject = new IdentifierReader {}

  override def spec = suite("IdentifierReader.identifier")(
    test("should succeed with valid identifiers") {
      // given
      val identifiers =
        List(
          "a",
          "bc",
          "abc",
          "$abc",
          "_01",
          "_00",
          "_abc01",
          "__",
          "aaa_01",
          "asda01_"
        )
      val expected    =
        List(
          Identifier("a"),
          Identifier("bc"),
          Identifier("abc"),
          Identifier("$abc"),
          Identifier("_01"),
          Identifier("_00"),
          Identifier("_abc01"),
          Identifier("__"),
          Identifier("aaa_01"),
          Identifier("asda01_")
        )
      for {
        // when
        responses <- ZIO.foreach(identifiers) { identifier =>
          zioFromParsed(parse(identifier, subject.test)).either
        }
        // then
      } yield assertTrue(responses.forall(_.isRight))
    },
    test("should fail on invalid identifiers") {
      // given
      val identifiers =
        List(
          "0",
          "000asdfa",
          "%%",
          "23",
          "@@",
          "!43#"
        )
      for {
        // when
        responses <- ZIO.foreach(identifiers) { identifier =>
          zioFromParsed(parse(identifier, subject.test)).either
        }
        // then
      } yield assertTrue(responses.forall(_.isLeft))
    }
  )
}

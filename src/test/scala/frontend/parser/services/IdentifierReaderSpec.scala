package chunk
package frontend.parser.services

import fastparse.*
import NoWhitespace.*
import frontend.parser.domain.*
import zio.*
import zio.test.*
import zio.test.Assertion.*

object IdentifierReaderSpec extends ZIOSpecDefault {
  private val subject = new IdentifierReader {}

  override def spec: Spec[Any, Nothing] = suite("IdentifierReader.identifier")(
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
          "asda01_",
          "personName"
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
          Identifier("asda01_"),
          Identifier("personName")
        )
      for {
        // when
        responses <- ZIO.foreach(identifiers) { identifier =>
          zioFromParsed(parse(identifier, subject.test)).either
        }
        // then
      } yield assertTrue(responses.map(_.map(_.value)) == expected.map(Right(_)))
    },
    test("should fail on invalid identifiers") {
      // given
      val identifiers =
        List(
          "0",
          "var a",
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
    },
    test("should fail with keywords") {
      // given
      val identifiers =
        List(
          "True",
          "False",
          "val",
          "var",
          "def",
          "fn",
          "trait",
          "class",
          "while",
          "do",
          "until",
          "private",
          "protected",
          "extends"
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

package chunk
package frontend.parser.services

import fastparse.*
import NoWhitespace.*
import frontend.parser.domain.*
import zio.*
import zio.test.*
import zio.test.Assertion.*

object IdentifierNameReaderSpec extends ZIOSpecDefault {

  private val subject = new IdentifierNameReader {}

  override def spec = suite("IdentifierReader.identifier")(
    test("should succeed with valid identifiers") {
      // given
      val identifiers =
        List(
          "a",
          "b.c",
          "a.b.c",
          "$abc",
          "_01",
          "_00",
          "_abc01",
          "__",
          "aaa_01",
          "personName"
        )
      val expected =
        List(
          IdentifierName(List(Identifier("a"))),
          IdentifierName(List(Identifier("b"), Identifier("c"))),
          IdentifierName(List(Identifier("a"), Identifier("b"), Identifier("c"))),
          IdentifierName(List(Identifier("$abc"))),
          IdentifierName(List(Identifier("_01"))),
          IdentifierName(List(Identifier("_00"))),
          IdentifierName(List(Identifier("_abc01"))),
          IdentifierName(List(Identifier("__"))),
          IdentifierName(List(Identifier("aaa_01"))),
          IdentifierName(List(Identifier("personName")))
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
          "000asdfa",
//          "%%",
          "23",
//          "@@",
          "!43#",
          "a.b..c",
          "_a.bc.01"
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

package chunk
package frontend.parser.services

import fastparse.*
import NoWhitespace.*
import frontend.parser.domain.*
import zio.*
import zio.test.*
import zio.test.Assertion.*

object ValueTokenReaderSpec extends ZIOSpecDefault {

  private val subject          = new ValueTokenReader {}
  private val givenAndExpected = List(
    ("a", ValueToken.ValueTokenIdentifier(IdentifierName(List(Identifier("a"))))),
    ("b.c", ValueToken.ValueTokenIdentifier(IdentifierName(List(Identifier("b"), Identifier("c"))))),
    ("a.b.c", ValueToken.ValueTokenIdentifier(IdentifierName(List(Identifier("a"), Identifier("b"), Identifier("c"))))),
    (
      "personName(ssd(s),22)",
      ValueToken.ValueTokenFunction(
        IdentifierName(List(Identifier("personName"))),
        params = List(
          ValueToken.ValueTokenFunction(
            IdentifierName(List(Identifier("ssd"))),
            params = List(ValueToken.ValueTokenIdentifier(IdentifierName(List(Identifier("s")))))
          ),
          ValueToken.ValueTokenLiteral(Literal.IntLiteral(22))
        )
      )
    ),
    (
      "abc[A, F[B], G[C,D[E]]](23)",
      ValueToken.ValueTokenFunction(
        IdentifierName(List(Identifier("abc"))),
        genericMembers = List(
          TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("A")))),
          TypeDef.HKTTypeDef(IdentifierName(List(Identifier("F"))), members = List(TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("B")))))),
          TypeDef.HKTTypeDef(
            IdentifierName(List(Identifier("G"))),
            members = List(
              TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("C")))),
              TypeDef.HKTTypeDef(
                IdentifierName(List(Identifier("D"))),
                members = List(TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("E")))))
              )
            )
          )
        ),
        params = List(
          ValueToken.ValueTokenLiteral(Literal.IntLiteral(23))
        )
      )
    ),
    (
      "_01(22)",
      ValueToken.ValueTokenFunction(
        IdentifierName(List(Identifier("_01"))),
        params = List(ValueToken.ValueTokenLiteral(Literal.IntLiteral(22)))
      )
    ),
    (
      "_a.b.c01(233,asdf)",
      ValueToken.ValueTokenFunction(
        IdentifierName(List(Identifier("_a"), Identifier("b"), Identifier("c01"))),
        params = List(
          ValueToken.ValueTokenLiteral(Literal.IntLiteral(233)),
          ValueToken.ValueTokenIdentifier(IdentifierName(List(Identifier("asdf"))))
        )
      )
    ),
    ("__", ValueToken.ValueTokenIdentifier(IdentifierName(List(Identifier("__"))))),
    ("aaa_01", ValueToken.ValueTokenIdentifier(IdentifierName(List(Identifier("aaa_01"))))),
    ("True", ValueToken.ValueTokenLiteral(Literal.BooleanLiteral(true))),
    ("22", ValueToken.ValueTokenLiteral(Literal.IntLiteral(22))),
    (""""hola"""", ValueToken.ValueTokenLiteral(Literal.StringLiteral("hola")))
  )

  private val invalidIdentifiers = List(
    "0s",
    "person[]()",
    "person[_]()",
    "000asdfa",
//    "%%",
//    "--",
//    "@@",
    "!43#",
    "a.b..c",
    "_a.bc.01"
  )

  override def spec: Spec[Any, Nothing] = suite("ValueTokenReader.reader")(
    suite("should succeed with valid identifier:")(for {
      (identifier, expected) <- givenAndExpected
    } yield test(identifier) {
      // given
      for {
        // when
        responses <- zioFromParsed(parse(identifier, subject.test)).either
        // then
      } yield assertTrue(responses.map(_.value) == Right(expected))
    }),

    // given
    suite("should fail on invalid identifier:")(for {
      identifier <- invalidIdentifiers
    } yield test(identifier) {
      for {
        // when
        response <- zioFromParsed(parse(identifier, subject.test)).either
        // then
      } yield assertTrue(response.isLeft)
    })
  )

}

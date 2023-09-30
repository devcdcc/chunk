package chunk
package frontend.parser.services

import frontend.parser.domain.*

import fastparse.*
import fastparse.NoWhitespace.*
import zio.*
import zio.prelude.data.Optional
import zio.test.*
import zio.test.Assertion.*

object AssignationReaderSpec extends ZIOSpecDefault {
  import TestAspect.*
  private val subject          = new AssignationReader {}
  private val givenAndExpected = List(
    (
      "a = 22",
      Assignation.ValueAssignation(
        Identifier("a"),
        typeDef = Optional.Absent,
        defaultValue = ValueToken.ValueTokenLiteral(Literal.IntLiteral(value = 22))
      )
    ),
    (
      "val a = 22",
      Assignation.ValueAssignation(
        Identifier("a"),
        typeDef = Optional.Absent,
        defaultValue = ValueToken.ValueTokenLiteral(Literal.IntLiteral(value = 22))
      )
    ),
    (
      "a: Boolean = True",
      Assignation.ValueAssignation(
        Identifier("a"),
        typeDef = TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("Boolean")))),
        defaultValue = ValueToken.ValueTokenLiteral(Literal.BooleanLiteral(true))
      )
    ),
    (
      "val a: Boolean = True",
      Assignation.ValueAssignation(
        Identifier("a"),
        typeDef = TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("Boolean")))),
        defaultValue = ValueToken.ValueTokenLiteral(Literal.BooleanLiteral(true))
      )
    ),
    (
      "var a: Boolean = True",
      Assignation.VarAssignation(
        Identifier("a"),
        typeDef = TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("Boolean")))),
        defaultValue = ValueToken.ValueTokenLiteral(Literal.BooleanLiteral(true))
      )
    )
  )

  private val invalidIdentifiers = List(
    "0s",
    "person[]()",
    "person[_]()",
    "000asdfa",
    "%%",
    "--",
    "@@",
    "!43#",
    "a.b..c",
    "_a.bc.01"
  )

  override def spec: Spec[Any, Nothing] = suite("MemberReader.reader")(
    suite("should succeed with valid member:")(for {
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

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
    ("a", Invocation.InvocationIdentifier(InvocationName(List(Identifier("a"))))),
    ("b.c", Invocation.InvocationIdentifier(InvocationName(List(Identifier("b"), Identifier("c"))))),
    ("a.b.c", Invocation.InvocationIdentifier(InvocationName(List(Identifier("a"), Identifier("b"), Identifier("c"))))),
    (
      "personName(ssd(s),22)",
      Invocation.InvocationFunction(
        InvocationName(List(Identifier("personName"))),
        params = List(
          Invocation.InvocationFunction(
            InvocationName(List(Identifier("ssd"))),
            params = List(Invocation.InvocationIdentifier(InvocationName(List(Identifier("s")))))
          ),
          Invocation.InvocationLiteral(Literal.IntLiteral(22))
        )
      )
    ),
    (
      "abc[A, F[B], G[C,D[E]]](23)",
      Invocation.InvocationFunction(
        InvocationName(List(Identifier("abc"))),
        genericMembers = List(
          TypeDef.SimpleTypeDef("A"),
          TypeDef.HKTTypeDef("F", members = List(TypeDef.SimpleTypeDef("B"))),
          TypeDef.HKTTypeDef(
            "G",
            members =
              List(TypeDef.SimpleTypeDef("C"), TypeDef.HKTTypeDef("D", members = List(TypeDef.SimpleTypeDef("E"))))
          )
        ),
        params = List(
          Invocation.InvocationLiteral(Literal.IntLiteral(23))
        )
      )
    ),
    (
      "_01(22)",
      Invocation.InvocationFunction(
        InvocationName(List(Identifier("_01"))),
        params = List(Invocation.InvocationLiteral(Literal.IntLiteral(22)))
      )
    ),
    (
      "_a.b.c01(233,asdf)",
      Invocation.InvocationFunction(
        InvocationName(List(Identifier("_a"), Identifier("b"), Identifier("c01"))),
        params = List(
          Invocation.InvocationLiteral(Literal.IntLiteral(233)),
          Invocation.InvocationIdentifier(InvocationName(List(Identifier("asdf"))))
        )
      )
    ),
    ("__", Invocation.InvocationIdentifier(InvocationName(List(Identifier("__"))))),
    ("aaa_01", Invocation.InvocationIdentifier(InvocationName(List(Identifier("aaa_01"))))),
    ("True", Invocation.InvocationLiteral(Literal.BooleanLiteral(true))),
    ("22", Invocation.InvocationLiteral(Literal.IntLiteral(22))),
    (""""hola"""", Invocation.InvocationLiteral(Literal.StringLiteral("hola")))
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

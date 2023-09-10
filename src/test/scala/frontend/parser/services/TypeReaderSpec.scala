package chunk
package frontend.parser.services

import zio.*
import zio.test.*
import fastparse.*
import NoWhitespace.*
import frontend.parser.domain.*

object TypeReaderSpec extends ZIOSpecDefault {

  private val givenAndExpected = List(
    ("aTypeName", TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("aTypeName"))))),
    (
      "a.full.package.name",
      TypeDef.SimpleTypeDef(
        IdentifierName(List(Identifier("a"), Identifier("full"), Identifier("package"), Identifier("name")))
      )
    ),
    ("ANUPPPERCASENAME", TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("ANUPPPERCASENAME"))))),
    ("ACamelCaseName", TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("ACamelCaseName"))))),
    ("lowercaseName", TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("lowercaseName"))))),
    ("H[_]", TypeDef.HKTTypeDef(IdentifierName(List(Identifier("H"))), List(TypeDef.InferredHole))),
    (
      "H[T]",
      TypeDef.HKTTypeDef(
        IdentifierName(List(Identifier("H"))),
        List(TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("T")))))
      )
    ),
    (
      "com.package.name.H[T]",
      TypeDef.HKTTypeDef(
        IdentifierName(List(Identifier("com"), Identifier("package"), Identifier("name"), Identifier("H"))),
        List(TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("T")))))
      )
    ),
    (
      "com.package.name.H[com.package.name.T]",
      TypeDef.HKTTypeDef(
        IdentifierName(List(Identifier("com"), Identifier("package"), Identifier("name"), Identifier("H"))),
        List(
          TypeDef.SimpleTypeDef(
            IdentifierName(List(Identifier("com"), Identifier("package"), Identifier("name"), Identifier("T")))
          )
        )
      )
    ),
    (
      "H[K,T]",
      TypeDef.HKTTypeDef(
        IdentifierName(List(Identifier("H"))),
        List(
          TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("K")))),
          TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("T"))))
        )
      )
    ),
    (
      "H[K[T,U]]",
      TypeDef.HKTTypeDef(
        IdentifierName(List(Identifier("H"))),
        List(
          TypeDef.HKTTypeDef(
            IdentifierName(List(Identifier("K"))),
            List(
              TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("T")))),
              TypeDef.SimpleTypeDef(IdentifierName(List(Identifier("U"))))
            )
          )
        )
      )
    )
  )

  override def spec: Spec[TestEnvironment with Scope, Any] = suite("TypeReader.typeReader")(
    suite("should succeed with valid typeDef")(
      for {
        (typeDef, expected) <- givenAndExpected
      } yield test(typeDef) {
        // given
        for {
          // when
          result <- zioFromParsed(parse(typeDef, TypeReader.test))

          // then
        } yield assertTrue(
          result.value == expected
        )
      }
    ),
    test("should fail for malformed types") {
      // given
      val typeDefs = List(
        ".aTypeName",
        "a.full.package.",
        ".",
        "ACamelCaseName,AXX",
        "H[T",
        "H[]",
        "HT]",
        "H[K,]",
        "0H[K]",
        "H[K[T,U]",
        "H[K[T,U,]]",
        "H[K[,T,U]]"
      )
      for {
        // when
        result: List[Either[Failed, Succeed[LangToken]]] <- ZIO.foreach(typeDefs) { typeDef =>
          zioFromParsed(parse(typeDef, TypeReader.test)).either
        }
        // then
      } yield assertTrue(
        result.forall(_.isLeft)
      )
    }
  )
}

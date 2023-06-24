package com.github.devcdcc.foop
package parser.services
import zio.*
import zio.test.*
import fastparse.*
import NoWhitespace.*
import parser.domain.*

object TypeReaderSpec extends ZIOSpecDefault {

  override def spec = suite("TypeReader.typeReader")(
    test("should succeed with valid typeDef") {
      // given
      val typeDefs = List(
        "aTypeName",
        "a.full.package.name",
        "ANUPPPERCASENAME",
        "ACamelCaseName",
        "lowercaseName",
        "H[_]",
        "H[T]",
        "com.package.name.H[T]",
        "com.package.name.H[com.package.name.T]",
        "H[K,T]",
        "H[K[T,U]]"
      )
      val expected = List(
        TypeDef.SimpleTypeDef("aTypeName"),
        TypeDef.SimpleTypeDef("a.full.package.name"),
        TypeDef.SimpleTypeDef("ANUPPPERCASENAME"),
        TypeDef.SimpleTypeDef("ACamelCaseName"),
        TypeDef.SimpleTypeDef("lowercaseName"),
        TypeDef.HKTTypeDef("H", List(TypeDef.InferredHole)),
        TypeDef.HKTTypeDef("H", List(TypeDef.SimpleTypeDef("T"))),
        TypeDef.HKTTypeDef("com.package.name.H", List(TypeDef.SimpleTypeDef("T"))),
        TypeDef.HKTTypeDef("com.package.name.H", List(TypeDef.SimpleTypeDef("com.package.name.T"))),
        TypeDef.HKTTypeDef("H", List(TypeDef.SimpleTypeDef("K"), TypeDef.SimpleTypeDef("T"))),
        TypeDef.HKTTypeDef(
          "H",
          List(TypeDef.HKTTypeDef("K", List(TypeDef.SimpleTypeDef("T"), TypeDef.SimpleTypeDef("U"))))
        )
      )
      for {
        // when
        result <- ZIO.foreach(typeDefs) { typeDef =>
          zioFromParsed(parse(typeDef, TypeReader.test))
        }
        // then
      } yield assertTrue(
        result.map(_.value) == expected &&
          result.map(_.index) == typeDefs.map(_.length)
      )
    },
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
        result: List[Either[Failed, Succeed[FoopToken]]] <- ZIO.foreach(typeDefs) { typeDef =>
          zioFromParsed(parse(typeDef, TypeReader.test)).either
        }
        // then
      } yield assertTrue(
        result.forall(_.isLeft)
      )
    }
  )
}

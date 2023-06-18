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
        "H[T]",
        "H[K,T]",
        "H[K[T,U]]"
      )
      val expected = List(
        SimpleTypeDef("aTypeName"),
        SimpleTypeDef("a.full.package.name"),
        SimpleTypeDef("ANUPPPERCASENAME"),
        SimpleTypeDef("ACamelCaseName"),
        SimpleTypeDef("lowercaseName"),
        HKTTypeDef("H", List(SimpleTypeDef("T"))),
        HKTTypeDef("H", List(SimpleTypeDef("K"), SimpleTypeDef("T"))),
        HKTTypeDef("H", List(HKTTypeDef("K", List(SimpleTypeDef("T"), SimpleTypeDef("U")))))
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

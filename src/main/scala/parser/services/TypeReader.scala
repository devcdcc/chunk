package com.github.devcdcc.foop
package parser.services

import fastparse.*
import NoWhitespace.*
import parser.domain.*

trait TypeReader extends BasicReader[TypeDef]:
  private def basicType[$: P]: P[SimpleTypeDef] = IdentifierReader.identifierText
    .rep(1, sep = ".")
    .map(seq => seq.mkString("."))
    .map(SimpleTypeDef.apply)
  private def hktType[$: P]: P[HKTTypeDef]      =
    P(
      IdentifierReader.identifierText
        .rep(1, sep = ".")
        .map(seq => seq.mkString(".")) ~ "[" ~ reader.rep(1, sep = ",") ~ "]"
    )
      .map((name, attrs) => HKTTypeDef(name, attrs))
  override def reader[$: P]: P[TypeDef]         = // basicType
    P(
      hktType | basicType
    )
end TypeReader

object TypeReader extends TypeReader:
end TypeReader

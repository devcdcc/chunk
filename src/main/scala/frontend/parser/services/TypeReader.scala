package chunk
package frontend.parser.services

import fastparse.*
import NoWhitespace.*
import frontend.parser.domain.*

trait TypeReader extends BasicReader[TypeDef]:
  private def basicType[$: P]: P[TypeDef.SimpleTypeDef] = IdentifierReader.identifierText
    .rep(1, sep = ".")
    .map(seq => seq.mkString("."))
    .map(TypeDef.SimpleTypeDef.apply)

  private def inferredByContext[$: P]: P[TypeDef] = "_".!.map(_ => TypeDef.InferredHole)

  private def hktType[$: P]: P[TypeDef.HKTTypeDef] =
    P(
      IdentifierReader.identifierText
        .rep(1, sep = ".")
        .map(seq => seq.mkString(".")) ~ "[" ~ (reader | inferredByContext)
        .rep(1, sep = ",") ~ "]"
    )
      .map((name, attrs) => TypeDef.HKTTypeDef(name, attrs))
  override def reader[$: P]: P[TypeDef]            = // basicType
    P(
      !P("_") ~ (hktType | basicType)
    )
end TypeReader

object TypeReader extends TypeReader:
end TypeReader

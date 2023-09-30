package chunk
package frontend.parser.services

import fastparse.*
import ScalaWhitespace.*
import frontend.parser.domain.*

import BasicReader.*
import frontend.parser.domain

trait ValueTokenReader extends BasicReader[ValueToken]:

  private def invocationIdentifier[$: P] =
    IdentifierNameReader.reader.map(name => ValueToken.ValueTokenIdentifier(name))

  private def invocationLiteral[$: P] = LiteralReader.reader.map(value => ValueToken.ValueTokenLiteral(value))

  private def hktGenericParam[$: P]: P[TypeDef] =
    P(
      IdentifierNameReader.reader ~ "[" ~ hktGenericParam
        .rep(1, sep = ",") ~ "]"
    )
      .map((name, attrs) => TypeDef.HKTTypeDef(name, attrs))
      | TypeReader.basicType.filter(_.name.name != List(Identifier("_")))

  private def genericParams[$: P]: P[Option[Seq[TypeDef]]] = P(
    "[" ~ hktGenericParam.rep(min = 1, sep = ",") ~ "]"
  ).?

  private def literal[$: P]    = P(LiteralReader.reader.map(ValueToken.ValueTokenLiteral.apply))
  private def invocation[$: P] = P(IdentifierNameReader.reader.map(name => ValueToken.ValueTokenIdentifier(name)))

  override def reader[$: P]: P[ValueToken] =
    P(
      (IdentifierNameReader.reader ~ genericParams ~ "(" ~ reader.rep(sep = ",") ~ ")").map {
        case (name, hktParams, params) =>
          ValueToken.ValueTokenFunction(name, hktParams.getOrElse(Nil).toList, params.toList)
      }
    ) | literal | invocation


end ValueTokenReader

object ValueTokenReader extends ValueTokenReader:
end ValueTokenReader

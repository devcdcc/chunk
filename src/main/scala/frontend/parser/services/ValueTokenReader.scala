package chunk
package frontend.parser.services

import fastparse.*
import ScalaWhitespace.*
import frontend.parser.domain.*

import BasicReader.*
import frontend.parser.domain

trait ValueTokenReader extends BasicReader[ValueToken]:

  private def invocationIdentifier[$: P] =
    InvocationNameReader.reader.map(name => Invocation.InvocationIdentifier(name))

  private def invocationLiteral[$: P] = LiteralReader.reader.map(value => Invocation.InvocationLiteral(value))

  private def hktGenericParam[$: P]: P[TypeDef] =
    P(
      InvocationNameReader.reader ~ "[" ~ hktGenericParam
        .rep(1, sep = ",") ~ "]"
    )
      .map((name, attrs) => TypeDef.HKTTypeDef(name, attrs))
      | TypeReader.basicType.filter(_.name.name != List(Identifier("_")))

  private def genericParams[$: P]: P[Option[Seq[TypeDef]]] = P(
    "[" ~ hktGenericParam.rep(min = 1, sep = ",") ~ "]"
  ).?

  private def literal[$: P]    = P(LiteralReader.reader.map(Invocation.InvocationLiteral.apply))
  private def invocation[$: P] = P(InvocationNameReader.reader.map(name => Invocation.InvocationIdentifier(name)))

  override def reader[$: P]: P[ValueToken] =
    P(
      (InvocationNameReader.reader ~ genericParams ~ "(" ~ reader.rep(sep = ",") ~ ")").map {
        case (name, hktParams, params) =>
          Invocation.InvocationFunction(name, hktParams.getOrElse(Nil).toList, params.toList)
      }
    ) | literal | invocation


end ValueTokenReader

object ValueTokenReader extends ValueTokenReader:
end ValueTokenReader

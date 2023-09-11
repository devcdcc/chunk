package chunk
package frontend.parser.services

import fastparse.*
import NoWhitespace.*
import frontend.parser.domain.*

import BasicReader.{separator, *}
import BasicReader.*
import zio.prelude.data.Optional

trait DeclarationReader extends BasicReader[Declaration]:
  import BasicReader.*

  private def valueDeclarationReader[$: P] = P(
    P(
      "val" ~ separator
    ).? ~ IdentifierReader.reader ~ separatorOpt ~ (":" ~ separatorOpt ~ TypeReader.reader).? ~ separatorOpt ~ "=" ~
      separatorOpt ~ ValueTokenReader.reader
  ).map { (identifier, typeValue, token) =>
    Declaration.ValueDeclaration(identifier, Optional.OptionIsNullable(typeValue), token)
  }

  private def variableDeclarationReader[$: P] = P(
    P("var" ~ separator)
      ~ IdentifierReader.reader ~ separatorOpt ~ (":" ~ separatorOpt ~ TypeReader.reader).? ~ separatorOpt ~ "=" ~
      separatorOpt ~ ValueTokenReader.reader.?
  ).map { (identifier, typeValue, token) =>
    println(identifier)
    Declaration.VarDeclaration(identifier, Optional.OptionIsNullable(typeValue), Optional.OptionIsNullable(token))
  }

  override def reader[$: P]: P[Declaration] = variableDeclarationReader | valueDeclarationReader

end DeclarationReader

object DeclarationReader extends DeclarationReader:
end DeclarationReader

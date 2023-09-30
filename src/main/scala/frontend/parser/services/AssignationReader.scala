package chunk
package frontend.parser.services

import fastparse.*
import NoWhitespace.*
import frontend.parser.domain.*

import BasicReader.{separator, *}
import BasicReader.*
import zio.prelude.data.Optional

trait AssignationReader extends BasicReader[Assignation]:
  import BasicReader.*

  def valueMemberReader[$: P] = P(
    P(
      "val" ~ separator
    ).? ~ IdentifierReader.reader ~ separatorOpt ~ (":" ~ separatorOpt ~ TypeReader.reader).? ~ separatorOpt ~ "=" ~
      separatorOpt ~ ValueTokenReader.reader
  ).map { (identifier, typeValue, token) =>
    Assignation.ValueAssignation(identifier, Optional.OptionIsNullable(typeValue), token)
  }

  def variableMemberReader[$: P] = P(
    P("var" ~ separator)
      ~ IdentifierReader.reader ~ separatorOpt ~ (":" ~ separatorOpt ~ TypeReader.reader).? ~ separatorOpt ~ "=" ~
      separatorOpt ~ ValueTokenReader.reader.?
  ).map { (identifier, typeValue, token) =>
    println(identifier)
    Assignation.VarAssignation(identifier, Optional.OptionIsNullable(typeValue), Optional.OptionIsNullable(token))
  }

//    .map{
//    (identifier: Identifier, typeValue: TypeDef, token: ValueToken) => ???
//  }

  override def reader[$: P]: P[Assignation] = variableMemberReader | valueMemberReader

end AssignationReader

object AssignationReader extends AssignationReader:
end AssignationReader

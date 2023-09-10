package chunk
package frontend.parser.services

import fastparse.*
import ScalaWhitespace.*

import frontend.parser.domain.*

trait IdentifierNameReader extends BasicReader[IdentifierName]:
  override def reader[$: P]: P[IdentifierName] = P(
    IdentifierReader.reader.rep(min = 1, sep = ".").map(identifiers => IdentifierName(identifiers.toList))
  )
end IdentifierNameReader

object IdentifierNameReader extends IdentifierNameReader:
end IdentifierNameReader

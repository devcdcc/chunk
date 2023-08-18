package chunk
package frontend.parser.services


import fastparse.*
import ScalaWhitespace.*

import frontend.parser.domain.*

trait InvocationNameReader extends BasicReader[InvocationName]:
  override def reader[$: P]: P[InvocationName] = P(IdentifierReader.reader.rep(sep = ".").map(identifiers =>
    InvocationName(identifiers.toList)
  ))
end InvocationNameReader

object InvocationNameReader extends InvocationNameReader:
end InvocationNameReader

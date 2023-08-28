package chunk
package frontend.parser.services

import fastparse.*
import NoWhitespace.*
import frontend.parser.domain.*

import zio.*

type Succeed[+T] = Parsed.Success[T]
sealed trait Failed
case class UnexpectedParserError(throwable: Throwable) extends Failed
case class ParserError(parsed: Parsed.Failure)         extends Failed

//type Failed      = Parsed.Failure | Throwable
def zioFromParsed[T >: LangToken](p: => Parsed[T]): IO[Failed, Succeed[T]] =
  ZIO
    .attemptUnsafe(_ => p)
    .mapError(error => UnexpectedParserError(error))
    .flatMap {
      case Parsed.Success(value, index) =>
        ZIO.succeed(Parsed.Success(value, index))
      case failure: Parsed.Failure      =>
        ZIO.fail(ParserError(failure))
    }

object ReaderHelper:
end ReaderHelper

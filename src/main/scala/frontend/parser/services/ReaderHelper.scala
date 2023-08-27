package chunk
package frontend.parser.services

import fastparse.*
import NoWhitespace.*
import frontend.parser.domain.*

import zio.*
//
//type Succeed[+T] = Parsed.Success[T]
//type Failed      = Parsed.Failure | Throwable
//def zioFromParsed[T >: FoopToken](p: => Parsed[T]): IO[Failed, Succeed[T]] =
//  ZIO
//    .attemptUnsafe(_ => p)
//    .mapError(error => Parsed.Failure(error.getMessage, 0))
//    .flatMap {
//      case Parsed.Success(value, index) =>
//        ZIO.succeed(Parsed.Success(value, index))
//      case failure: Parsed.Failure      =>
//        ZIO.fail(failure)
//    }
//object ReaderHelper:
//end ReaderHelper

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
//extension [T](p: => P[T])
//  def fullEval(implicit ctx: P[_]): P[T] = Start ~ p ~ End
//  def d[$: P]: P[T]                      = Start ~ p ~ End
//
//def FullEval[$: P, T](p: => P[T], pr: ParsingRun[$])(implicit ctx: P[_]): P[T] = {
//  val res = p
//  Start ~ res ~ End
//}

object ReaderHelper:
end ReaderHelper

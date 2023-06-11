package com.github.devcdcc.foop
package parser.services

import fastparse.*
import NoWhitespace.*
import parser.domain.*
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
def zioFromParsed[T >: FoopToken](p: => Parsed[T]): IO[Failed, Succeed[T]] =
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

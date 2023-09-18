package chunk

import zio.stream.*
import zio.*
import fastparse.*
import NoWhitespace.*
import frontend.parser.domain
import frontend.parser.services.*

import chunk.frontend.parser.scalaparse.Scala

object Main extends ZIOAppDefault {
  override def run =
    for {
      r <-
        zioFromParsed(
          parse(
            """package chunk
              |
              |import zio.stream.*
              |import zio.*
              |import fastparse.*
              |
              |object Main {
              |  def main(args: Array[String]): Unit =
              |    for {
              |       _ <- a
              |       b <- c
              |    } yield ()
              |    asdf(a => {a * 2})
              |    if (true) then
              |    println("2")
              |    println("Hello World!")
              |  }
              |
              |
              |""".stripMargin,
            Scala.CompilationUnit
          )
        )
      _ <- Console.printLine(r)
    } yield ()
//  override def run = {
//    for {
//      r <- TokensReader.firstOrderStatement(
//        """   sum(a: Int, b: Int): Int
//          |sum(a: Int, b: Int)
//          |
//          |sum(a, b)""".stripMargin);
//      q <- ZStream.unit.mapZIO(_ => ZIO.console.flatMap(_.printLine(r)));
//    } yield ()
//  }.provideLayer(TokensReader.layer)
//    .runCollect
//    .exitCode
}

package chunk

import zio.stream.*
import zio.*
import fastparse.*
import NoWhitespace.*
import frontend.parser.domain

object Main extends ZIOAppDefault {
  override def run =
    for {
      r <- ZIO.attemptUnsafe { _ =>
        "a" // parse("\"hello \\\"world\\\\ \\\" \"", domain.string).toString
      }
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

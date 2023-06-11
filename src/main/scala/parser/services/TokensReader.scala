/*
 * MIT License
 *
 * Copyright (c) 2023 Carlos Daniel Cañon Carrero.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.github.devcdcc.foop
package parser.services

import zio.*
import zio.stream.*
import parser.domain.*

//import com.github.devcdcc.foop.parser.antlr.*
//import org.antlr.v4.runtime.*
//import org.antlr.v4.runtime.tree.*

//import scala.jdk.CollectionConverters.*

trait TokensReader {
  def firstOrderStatement(content: String): ZStream[Any, Nothing, FoopToken]
}

private case class TokensReaderLive() extends TokensReader:
  override def firstOrderStatement(content: String): ZStream[Any, Nothing, FoopToken] = ???
//    val stream = CharStreams.fromString(content)
//    val lexer = new FOOPLexer(stream)
//    val tokens = new CommonTokenStream(lexer)
//    val parser = new FOOPParser(tokens)
//    val tree = parser.expr()
//    ZStream.fromIterable(tree.children.asScala
//      .map(_.asInstanceOf[TerminalNode]))
//      .filter(_.getSymbol.getType != FOOPParser.NEWLINE)
//      .map(_.getSymbol)
//      .map(AnyToken(_))

object TokensReader:
  def firstOrderStatement(content: String): ZStream[TokensReader, Nothing, FoopToken] = ZStream
    .serviceWithStream[TokensReader](_.firstOrderStatement(content))

  val layer = ZLayer.fromZIO(ZIO.succeed(TokensReaderLive()))

end TokensReader

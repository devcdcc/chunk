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

package chunk
package frontend.parser.services

import fastparse.*
import NoWhitespace.*
import frontend.parser.domain.*

trait LiteralReader extends BasicReader[Literal]:

  import BasicReader.*
  import frontend.parser.scalaparse.Scala
  private def booleanLiteral[$: P] = Scala.Literals.Bool.!.map(_.toBoolean).map(Literal.BooleanLiteral.apply)
  private def intLiteral[$: P]     = Scala.Literals.Int.!.map(_.toLong).map(Literal.IntLiteral.apply)
  private def doubleLiteral[$: P]  = Scala.Literals.Float.!.map(_.toDouble).map(Literal.DoubleLiteral.apply)
  private def stringLiteral[$: P]  = string.map(Literal.StringLiteral.apply)

  override def reader[$: P]: P[Literal] = P(
    stringLiteral | doubleLiteral | intLiteral | booleanLiteral
  )
end LiteralReader

object LiteralReader extends LiteralReader:

end LiteralReader

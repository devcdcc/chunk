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
import CharPredicates.*
import ScalaWhitespace.*

trait BasicReader[ReturnType]:
  def reader[$: P]: P[ReturnType]
  private[services] final def test[$: P]: P[ReturnType] =
    Start ~ reader ~ End

end BasicReader

object BasicReader:

  private def stringChars(c: Char)       = c != '\"' && c != '\\'
  private inline def separator[$: P]     = P(CharsWhileIn(" \r\n", 0))
  inline def digit[$: P]                 = P(CharIn("0-9"))
  inline def digits[$: P]                = P(digit.rep(min=1))
  inline def double[$: P]                = P(digits ~ "." ~ digits)
  private inline def hexDigit[$: P]      = P(CharIn("0-9a-fA-F"))
  private inline def unicodeEscape[$: P] = P("u" ~ hexDigit ~ hexDigit ~ hexDigit ~ hexDigit)
  private inline def escape[$: P]        = P("\\" ~ (CharIn("\"/\\\\bfnrt") | unicodeEscape))
  private inline def strChars[$: P]      = P(CharsWhile(stringChars))
  inline def string[$: P]                = P("\"" ~/ (strChars | escape).rep.! ~ "\"")
  inline def lowerCases[$: P]            = P(CharIn("a-z"))
  inline def upperCases[$: P]            = P(CharIn("A-Z"))

end BasicReader

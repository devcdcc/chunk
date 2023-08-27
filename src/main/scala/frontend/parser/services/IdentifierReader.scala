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
import ScalaWhitespace.*

import frontend.parser.domain.Identifier

trait IdentifierReader extends BasicReader[Identifier]:

  import BasicReader.*

  private inline def specialCharacterIdentifier[$: P] = P(CharIn("_$"))
  private inline def basicCharIdentifier[$: P]        = P(lowerCases | upperCases | specialCharacterIdentifier)
  private inline def basicMixedIdentifier[$: P]       = P(basicCharIdentifier | digit)
  private def identifierText[$: P]                            = P(basicCharIdentifier ~ basicMixedIdentifier.rep).!
  override def reader[$: P]: P[Identifier]            = identifierText.filter(a => a != "True" && a  != "False").map(Identifier.apply)
end IdentifierReader

object IdentifierReader extends IdentifierReader:
end IdentifierReader

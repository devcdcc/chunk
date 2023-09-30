package chunk
package frontend.parser.scalaparse.syntax

import fastparse.*
import fastparse.*
import NoWhitespace.*
import Basic.*
import CharPredicates.*
import chunk.frontend.parser.domain.Identifier
object Identifiers {

  case class NamedFunction(f: Char => Boolean)(implicit name: sourcecode.Name) extends (Char => Boolean) {
    def apply(t: Char)      = f(t)
    override def toString() = name.value

  }
  val OpCharNotSlash = NamedFunction(x => isOpChar(x) && x != '/')
  val NotBackTick    = NamedFunction(_ != '`')

  def Operator[$: P] = P(
    !SymbolicKeywords ~ (!StringIn("/*", "//") ~ (CharsWhile(OpCharNotSlash) | "/")).rep(1)
  ).opaque("operator")

  def VarId[$: P] = P(VarId0(true)).opaque("var-id")

  def VarId0[$: P](dollar: Boolean) = P(!Keywords ~ Lower ~ IdRest(dollar))

  def UppercaseId[$: P](dollar: Boolean) = P(!Keywords ~ Upper ~ IdRest(dollar))
  def PlainId[$: P]                      = P(UppercaseId(true) | VarId | Operator ~ (!OpChar | &(StringIn("/*", "//"))))
    .opaque("plain-id")

  def PlainIdNoDollar[$: P] = P(UppercaseId(false) | VarId0(false) | Operator).opaque("plain-id")

  def BacktickId[$: P]  = P("`" ~ CharsWhile(NotBackTick) ~ "`")
  def Id[$: P]: P[Identifier] = P(BacktickId | PlainId).opaque("id").!.map(Identifier.apply)

  def IdRest[$: P](allowDollar: Boolean) = {

    val IdCharacter =
      if (allowDollar) NamedFunction(c => c == '$' || isLetter(c) || isDigit(c))
      else NamedFunction(c => isLetter(c) || isDigit(c))

    def IdUnderscoreChunk = P(CharsWhileIn("_", 0) ~ CharsWhile(IdCharacter))
    P(IdUnderscoreChunk.rep ~ (CharsWhileIn("_") ~ CharsWhile(isOpChar, 0)).?)
  }

  def AlphabetKeywords[$: P] = P {
    StringIn(
      "abstract",
      "case",
      "catch",
      "class",
      "def",
      "do",
      "else",
      "extends",
      "False",
      "finally",
      "final",
      "finally",
      "fn",
      "forSome",
      "for",
      "if",
      "implicit",
      "import",
      "lazy",
      "match",
      "new",
      "null",
      "object",
      "override",
      "package",
      "private",
      "protected",
      "return",
      "sealed",
      "super",
      "this",
      "throw",
      "trait",
      "try",
      "True",
      "type",
      "until",
      "val",
      "var",
      "while",
      "with",
      "yield",
      "_",
      "macro"
    ) ~
      !CharPred(Basic.LetterDigitDollarUnderscore)
  }.opaque("AlphabetKeywords")

  def SymbolicKeywords[$: P] = P {
    StringIn(":", ";", "=>", "=", "<-", "<:", "<%", ">:", "#", "@", "\u21d2", "\u2190") ~ !OpChar
  }.opaque("SymbolicKeywords")

  def Keywords[$: P] = P(AlphabetKeywords | SymbolicKeywords)
}

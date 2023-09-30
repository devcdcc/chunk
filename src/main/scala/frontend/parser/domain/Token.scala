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

package frontend.parser.domain

import zio.prelude.data.*

val LANG_DEFAULT_PACKAGE = "chunk"

sealed trait LangToken

// Literal, used for identifier literal
sealed trait Literal                 extends LangToken
object Literal:
  case class IntLiteral(value: Long)        extends Literal
  case class DoubleLiteral(value: Double)   extends Literal
  case class StringLiteral(value: String)   extends Literal
  case class BooleanLiteral(value: Boolean) extends Literal
// identifier used for identifier declaration
case class Identifier(value: String) extends LangToken

// Type def used for type descriptions
sealed trait TypeDef                              extends LangToken
object TypeDef:
  case object InferredHole                                           extends TypeDef
  case class SimpleTypeDef(name: IdentifierName)                     extends TypeDef
  case class HKTTypeDef(name: IdentifierName, members: Seq[TypeDef]) extends TypeDef
end TypeDef

// InvocationName used for identifier/variables usage
case class IdentifierName(name: List[Identifier]) extends LangToken

//statement
// used for any sentence variable declaration or value/variable usage.
//type Statement = Declaration | Invocation | Evaluable // should add class/trait/object declarations
sealed trait Statement extends LangToken

// invocation used for variables usage
trait ValueToken extends Statement
object ValueToken:
  case class ValueTokenLiteral(literal: Literal)        extends ValueToken
  case class ValueTokenIdentifier(name: IdentifierName) extends ValueToken
  case class ValueTokenFunction(
    name: IdentifierName,
    genericMembers: Seq[TypeDef] = Nil,
    params: Seq[ValueToken] = Nil
  ) extends ValueToken

  sealed trait ValueTokenBlock extends ValueToken
  object ValueTokenBlock:

    sealed trait ParamDeclaration
    case class ParamValueDeclaration(name: Identifier, typeDef: Optional[TypeDef], defaultValue: Optional[ValueToken])
        extends ParamDeclaration
    case class ParamVariableDeclaration(
      name: Identifier,
      typeDef: Optional[TypeDef],
      defaultValue: Optional[ValueToken]
    ) extends ParamDeclaration

    case class SimpleBlock(statements: Seq[Statement]) extends ValueTokenBlock
    case class FunctionBlock(
      params: Seq[ParamValueDeclaration],
      returnType: Optional[TypeDef],
      statements: Seq[Statement]
    ) extends ValueTokenBlock
  end ValueTokenBlock

end ValueToken

// used for literals or variables usage
//type ValueToken = Invocation // | Literal

// used for defining variables or class members
sealed trait Declaration extends Statement
object Declaration:
  case class ValueDeclaration(name: Identifier, typeDef: Optional[TypeDef], defaultValue: ValueToken)
      extends Declaration
  case class MultipleValuesDeclaration(identifiers: Seq[(Identifier, Optional[TypeDef])], defaultValue: ValueToken)
      extends Declaration
  case class VarDeclaration(name: Identifier, typeDef: Optional[TypeDef], defaultValue: Optional[ValueToken])
      extends Declaration
  case class MethodDeclaration(
    name: Identifier,
    genericParams: Seq[TypeDef],
    params: Seq[Declaration],
    returnType: TypeDef,
    body: ValueToken
  ) extends Declaration
end Declaration

//type Assignabkle = Literal | Identifier
//
//sealed trait AbstractValueDeclaration(
//  name: Identifier,
//  typeDef: TypeDef,
//  defaultValue: Optional[Assignable] = Optional.Absent
//) extends FoopToken
//
//case class ValueDeclaration(name: Identifier, typeDef: TypeDef, defaultValue: Optional[Assignable] = Optional.Absent)
//    extends AbstractValueDeclaration(name = name, typeDef = typeDef, defaultValue = defaultValue)
//
//sealed trait LocationDef extends TypeDef
//case object LocationDef  extends LocationDef
//sealed trait GenericDef  extends TypeDef
//
//sealed trait AttributeDef extends FoopToken
//
//sealed trait MemberDef extends FoopToken
//object MemberDef:
//  type Statement = ValueDeclaration | Identifier | MemberDef
//  case class ValueMemberDef(name: Identifier, typeDef: TypeDef, defaultValue: Optional[Assignable] = Optional.Absent)
//      extends AbstractValueDeclaration(name = name, typeDef = typeDef, defaultValue = defaultValue)
//      with MemberDef
//  case class MethodMemberDef(
//    name: Identifier,
//    returnType: TypeDef,
//    generics: Seq[GenericDef],
//    attributes: List[ValueDeclaration],
//    statements: List[Statement]
//  ) extends MemberDef
//end MemberDef
//
//sealed trait TypeDef extends FoopToken:
//end TypeDef
//
//object TypeDef:
//
//  case object Unit   extends TypeDef
//  case object Bool   extends TypeDef
//  case object Byte   extends TypeDef
//  case object Int    extends TypeDef
//  case object Long   extends TypeDef
//  case object Float  extends TypeDef
//  case object Double extends TypeDef
//  case object String extends TypeDef
//
//  case class ClassTypeDef(
//    name: Identifier,
//    location: LocationDef,
//    generics: Seq[GenericDef],
//    attributes: List[AttributeDef],
//    members: Array[MemberDef]
//  ) extends TypeDef
//end TypeDef

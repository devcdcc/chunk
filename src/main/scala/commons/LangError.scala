package com.github.devcdcc.foop
package commons

trait ErrorLocation
case object UnknownLocation                                     extends ErrorLocation
case class KnownLocation(filename: String, line: Int, col: Int) extends ErrorLocation

case class LangError(message: String, location: ErrorLocation = UnknownLocation) extends Exception(message) {}

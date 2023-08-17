package chunk
package frontend.generator.services

import frontend.generator.domain.*

import zio.*
import zio.stream.ZStream

trait Generator {
  def gen(token: Token): ZStream[Any, Nothing, Instructions]
}

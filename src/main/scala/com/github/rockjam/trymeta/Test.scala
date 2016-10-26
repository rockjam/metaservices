/*
 * Copyright 2016 Nikolay Tatarinov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rockjam.trymeta

@listreq
object Requests {

  sealed trait Request

  case class GetName(id: Int) extends Request

  case class SetName(id: Int, name: String) extends Request

  case class FindUser(query: String) extends Request

}

object Main extends App {

  import Requests._
  import JsonFormatters._

  import io.circe._
  import io.circe.syntax._
  import io.circe.parser

  val initial = GetName(22)
  println(s"======initial: $initial")

  val there = initial.asJson.noSpaces
  println(s"======there: $there")

  val back =
    parser.decode[Request](there).getOrElse(sys.error("Failed to decode"))
  println(s"======back: $back")

  assert(initial == back)

}

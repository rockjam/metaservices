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

package com.github.rockjam.trymeta.model

import com.github.rockjam.trymeta.{ jsonrpc, rpc, service }

// probably should be close to impl, but not visible in the rest of project
@service
@jsonrpc
object Users {

  // TODO: looks awkward
  import rpc.Rpc._

  case class GetName(id: Int) extends Request[ResponseGetName]

  case class ResponseGetName(name: String) extends Response

  case class SetName(id: Int, name: String) extends Request[ResponseSetName]

  case class ResponseSetName() extends Response // TODO: make it case object

  case class FindUser(query: String) extends Request[ResponseFindUser]

  case class ResponseFindUser(users: List[String]) extends Response

}

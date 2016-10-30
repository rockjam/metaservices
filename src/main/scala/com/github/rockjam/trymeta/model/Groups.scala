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

import com.github.rockjam.trymeta.rpc.Rpc.{ Request, Response }
import com.github.rockjam.trymeta.{ jsonrpc, service }

@service
@jsonrpc
object Groups {

  case class Create(id: Int, title: String) extends Request[CreateAck]

  case class CreateAck() extends Response

  case class FindGroup(name: String) extends Request[FindGroupResponse]

  case class FindGroupResponse(groups: List[GroupRef]) extends Response

  case class GroupRef(id: Int, title: Option[String])

  case class GetTitle(id: Int) extends Request[ResponseGetTitle]

  case class ResponseGetTitle(name: String) extends Response

  case class SetTitle(id: Int, title: String) extends Request[ResponseSetTitle]

  case class ResponseSetTitle() extends Response // TODO: make it case object

}

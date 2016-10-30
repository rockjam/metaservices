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

package com.github.rockjam.trymeta.service
package impl

import cats.data.Xor
import models.Groups._

import scala.concurrent.{ ExecutionContext, Future }

final class GroupsServiceImpl(implicit ec: ExecutionContext) extends GroupsService {

  override def handleCreate(id: Int, title: String): Future[Xor[ServiceError, CreateAck]] = ???

  override def handleFindGroup(name: String): Future[Xor[ServiceError, FindGroupResponse]] = {
    val groups = List(
      GroupRef(22, Some("title of the group")),
      GroupRef(23, None),
      GroupRef(24, Some("other title of the group"))
    )
    Future.successful(Xor.Right(FindGroupResponse(groups)))
  }

  override def handleGetTitle(id: Int): Future[Xor[ServiceError, ResponseGetTitle]] = ???

  override def handleSetTitle(id: Int,
                              title: String): Future[Xor[ServiceError, ResponseSetTitle]] =
    ???
}

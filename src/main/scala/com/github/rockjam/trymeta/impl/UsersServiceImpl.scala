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

package com.github.rockjam.trymeta.impl

import scala.concurrent.{ ExecutionContext, Future }
import cats.data.Xor
import com.github.rockjam.trymeta.model.Users._
import com.github.rockjam.trymeta.rpc._

final class UsersServiceImpl(implicit ec: ExecutionContext)
    extends UsersService {

  override def handleFindUser(
      query: String): Future[Xor[Rpc.Error, ResponseFindUser]] =
    Future(List("rockjam", "charliebubbles"))
      .map(_.map(_.toUpperCase))
      .map(res ⇒ Xor.Right(ResponseFindUser(res)))

  override def handleGetName(
      id: Int): Future[Xor[Rpc.Error, ResponseGetName]] =
    Future.successful(Xor.Right(ResponseGetName("rockjam")))

  override def handleSetName(
      id: Int,
      name: String): Future[Xor[Rpc.Error, ResponseSetName]] =
    Future.successful(Xor.Right(ResponseSetName()))

}

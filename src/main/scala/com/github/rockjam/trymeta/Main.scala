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

import com.github.rockjam.trymeta.impl.UsersServiceImpl

//object HttpHandler {
//  import Users.JsonFormatters._ //???wtf??? why should I do it?
//
//  val usersService = new UsersServiceImpl()
//
//  def endpoint: Endpoint[???] = post(???) { e =>
//    usersService.handle(e)
//  }
//
//  Http.server.serve(":8080", endpoint.toServiceAs[???])
//
//}

import scala.concurrent.duration._
import scala.concurrent.Await

object Main extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val usersService = new UsersServiceImpl

  import com.github.rockjam.trymeta.model.Users._

  private val usersJsonRpc = new UsersJsonRpc(usersService)

  val result = Await.result(usersService.handleFindUser("bla"), 5.seconds)
  println(s"===find user result: ${result}")
}

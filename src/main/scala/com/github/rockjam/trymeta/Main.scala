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

import com.github.rockjam.trymeta.impl.{ GroupsServiceImpl, UsersServiceImpl }
import com.github.rockjam.trymeta.jsonrpc20.{ JsonRpcHub, JsonRpcService }

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
import com.github.rockjam.trymeta.model.Users
import com.github.rockjam.trymeta.model.Groups

import scala.concurrent.duration._
import scala.concurrent.Await

object Main extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val usersService  = new UsersServiceImpl
  private val groupsService = new GroupsServiceImpl

  private val usersJsonRpc: JsonRpcService  = new Users.UsersJsonRpc(usersService)
  private val groupsJsonRpc: JsonRpcService = new Groups.GroupsJsonRpc(groupsService)

  private val jsonRpc = new JsonRpcHub(
    List(
      usersJsonRpc,
      groupsJsonRpc
    )
  )

  import io.circe._, io.circe.syntax._, io.circe.generic.auto._

  val req = Groups.FindGroup("some group").asJson

  val result = Await.result(groupsJsonRpc.handleRequest(req)("Groups.FindGroup"), 5 seconds)

  println(result)

}

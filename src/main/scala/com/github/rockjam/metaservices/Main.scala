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

package com.github.rockjam.metaservices

import com.github.rockjam.metaservices.service.impl._
import com.github.rockjam.metaservices.jsonrpc.{ JsonRpc, JsonRpcRequestEnvelope }
import com.github.rockjam.metaservices.service.models.{ Calculator, Groups, Users }

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

  private val usersService      = new UsersServiceImpl
  private val groupsService     = new GroupsServiceImpl
  private val calculatorService = new CalculatorServiceImpl

  private val usersJsonRpc      = new Users.UsersJsonRpc(usersService)
  private val groupsJsonRpc     = new Groups.GroupsJsonRpc(groupsService)
  private val calculatorJsonRpc = new Calculator.CalculatorJsonRpc(calculatorService)

  private val jsonRpc = new JsonRpc(
    List(
      usersJsonRpc,
      groupsJsonRpc,
      calculatorJsonRpc
    )
  )

  import io.circe._, io.circe.syntax._, io.circe.generic.auto._

  val req = JsonRpcRequestEnvelope(
    Some("123"),
    "Calculator.Add",
    Calculator.Add(Some(1), Some(2)).asJson
  ).asJson

  println(req)

  val result = Await.result(jsonRpc.handle(req), 5 seconds)

  val req1 = JsonRpcRequestEnvelope(
    Some("123"),
    "Calculator.AZAZ",
    Calculator.Add(Some(1), Some(2)).asJson
  ).asJson

  println(req1)

  val result1 = Await.result(jsonRpc.handle(req1), 5 seconds)

  println(result)
  println(result1)

}

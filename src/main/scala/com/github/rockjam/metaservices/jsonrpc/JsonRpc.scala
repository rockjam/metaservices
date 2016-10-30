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

package com.github.rockjam.metaservices.jsonrpc

import cats.data.Xor

import scala.concurrent.{ ExecutionContext, Future }

final class JsonRpc(services: List[JsonRpcService])(implicit ec: ExecutionContext) {
  import io.circe._, io.circe.syntax._, io.circe.generic.auto._

  private val chain: Json ⇒ PartialFunction[String, Future[JsonRpcError Xor Json]] = { json ⇒
    val errCase: PartialFunction[String, Future[Xor[JsonRpcError, Json]]] = {
      case _ ⇒ Future.successful(Xor.Left(JsonRpcErrors.MethodNotFound))
    }

    (services.reverse foldRight errCase) { (el, acc) ⇒
      el.handleRequest(json) orElse acc
    }
  }

  def handle(req: Json): Future[Json] =
    req.as[JsonRpcRequestEnvelope] map { rpcReq ⇒
      // TODO: rcpReq validation
      chain(rpcReq.params)(rpcReq.method) map {
        case Xor.Right(json) ⇒ result(rpcReq.id, json)
        case Xor.Left(err)   ⇒ error(err)
      }
    } getOrElse Future.successful(error(JsonRpcErrors.ParseError))

  private def error(e: JsonRpcError) = JsonRpcResponseEnvelope(None, None, Some(e)).asJson
  private def result(id: Option[String], r: Json) =
    JsonRpcResponseEnvelope(id, Some(r), None).asJson

}

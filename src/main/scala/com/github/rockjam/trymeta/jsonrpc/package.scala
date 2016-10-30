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

import io.circe.Json

package object jsonrpc {
  case class JsonRpcRequestEnvelope(id: Option[String],
                                    method: String,
                                    params: Json,
                                    jsonrpc: String = "2.0")

  case class JsonRpcError(code: Int, message: String, data: Option[String] = None)

  case class JsonRpcResponseEnvelope(id: Option[String],
                                     result: Option[Json],
                                     error: Option[JsonRpcError],
                                     jsonrpc: String = "2.0")

  object JsonRpcErrors {
    val MethodNotFound = JsonRpcError(-32601, "Method not found")
    val ParseError     = JsonRpcError(-32700, "Parse error")
    val InvalidParams  = JsonRpcError(-32602, "Invalid params")
  }

}

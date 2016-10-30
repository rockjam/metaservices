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

import cats.data.Xor

package object service {
  type Result[T <: ServiceResponse] = ServiceError Xor T

  trait ServiceRequest[Resp <: ServiceResponse]
  trait ServiceResponse
  case class ServiceError(code: Int, message: String, data: Option[Array[Byte]])

  //TODO: looks ugly
  val ResponseVoidInstance = ResponseVoid()
  case class ResponseVoid() extends ServiceResponse

  object ServiceErrors {
    val ValidationError = ServiceError(400, "Validation failed", None)
  }
}

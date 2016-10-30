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

package com.github.rockjam.metaservices.service
package models

import com.github.rockjam.metaservices.{ generatedJsonrpc, generatedService }

@generatedService
@generatedJsonrpc
object Users {

  case class GetName(id: Int) extends ServiceRequest[ResponseGetName]

  case class ResponseGetName(name: String) extends ServiceResponse

  case class SetName(id: Int, name: String) extends ServiceRequest[ResponseVoid]

  case class FindUser(query: String) extends ServiceRequest[ResponseFindUser]

  case class ResponseFindUser(users: List[String]) extends ServiceResponse

}

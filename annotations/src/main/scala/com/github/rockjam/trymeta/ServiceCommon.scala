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

import scala.meta._

case class MethodDescription(req: Type,
                             resp: Type,
                             paramss: Seq[Seq[Term.Param]])

object ServiceCommon {
  def extractRpcRequests(stats: Seq[Stat]): Seq[MethodDescription] =
    (stats collect {
      case c: Defn.Class ⇒
        val q"..$_ class $reqType[..$_] ..$_ (...$paramss) extends $template" =
          c
        val template"{ ..$_ } with ..$ctorcalls { $_ => ..$_ }" = template

        println(s"===ctorcalls: $ctorcalls")

        val requestsClass = "com.github.rockjam.trymeta.rpc.Rpc.Request"

        val responseType = (ctorcalls: Seq[Ctor.Call]) flatMap (_.children) collect {
          case q"$expr[..$tpesnel]" ⇒
            requestsClass.endsWith(expr.syntax)
            tpesnel.head
        }
        responseType.headOption map (respType ⇒
                                       MethodDescription(reqType,
                                                         respType,
                                                         paramss))
    }).flatten
}

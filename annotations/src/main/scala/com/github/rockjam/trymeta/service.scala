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

import scala.annotation.StaticAnnotation
import scala.meta._

class service extends StaticAnnotation {

  inline def apply(defn: Any): Any = meta {

    println("============ in service")
    def aux(defn: Tree) = {
      val q"..$mods object $name { ..${stats: Seq[Stat]} }" = defn

      val serviceTrait = {
        val requests: Seq[MethodDescription] = ServiceCommon.extractRpcRequests(stats)

        println(s"=========requests are: ${requests}")

        val declarations = requests map { case MethodDescription(reqType, respType, paramss)=>
          val methodName = Term.Name(s"handle$reqType") // duplicate
          q"def $methodName(..${paramss.flatten}): Future[Xor[Error, $respType]]"
        }

        val serviceName = Type.Name(name.value + "Service")

        val imports = {
          val i = List(
            importer"cats.data.Xor",
            importer"com.github.rockjam.trymeta.rpc.Rpc._",
            importer"scala.concurrent.Future"
          )
          q"import ..$i"
        }

        q"""
          trait $serviceName {
            $imports
            ..$declarations
          }
        """
      }

      Utils.logResult(q"..$mods object $name { ..$stats; $serviceTrait }")
    }

    aux(defn)
  }



}

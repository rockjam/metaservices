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
        val requests: Seq[Defn.Class] = stats collect {
          case c: Defn.Class => c
        }

        val declarations = requests map { req =>
          val q"..$_ class $tname[..$_] ..$_ (...$paramss) extends $_" = req
          val methodName = Term.Name(s"handle${tname.value}")
          q"def $methodName(..${paramss.flatten}): Unit"
        }

        val serviceName = Type.Name(name.value + "Service")

        q"""
          trait $serviceName {
            ..$declarations
          }
        """
      }

      val result = q"..$mods object $name { ..$stats; $serviceTrait }"
      println(s"\n $result \n")
      result
    }

    aux(defn)
  }



}

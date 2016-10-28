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

class inspectClass extends StaticAnnotation {

  inline def apply(defn: Any): Any = meta {
    println("============ in extract")
    val q"..$mods class $tname[..$tparams] ..$othermods (...$paramss) extends $temp" = defn

    println("=================class")
    println(s"mods: ${mods}")
    println(s"tname: ${tname}")
    println(s"tparams: ${tparams}")
    println(s"othermods: ${othermods}")
    println(s"paramss: ${paramss}")
    println(s"temp: ${temp}")

    val template"{ ..$earlyStats } with ..$ctorcalls { $param => ..$stats }" = temp

    println(s"earlyStats: ${earlyStats}")
    println(s"ctorcalls: ${ctorcalls}")
    println(s"param: ${param}")
    println(s"stats: ${stats}")

    println("{==============================")
    (ctorcalls: Seq[Ctor.Call]) flatMap(_.children) collect {
      case q"$expr[..$tpesnel]"  =>

        val ctorSelection = ctor"Baz.BarBase"
        println(s"=========ctorSelection.structure: ${ctorSelection.structure}")
        println(s"=========ctorSelection.syntax: ${ctorSelection.syntax}")

        println(s"=======ctorSelection: ${ctorSelection}")
        println(s"=======ctorSelection class: ${ctorSelection.getClass}")

        println(s"====eq: ${ctorSelection.syntax == expr.syntax}")
        println(s"===========expr: ${expr}")
        println(s"===========expr class: ${expr.getClass}")

        println(s"===========tpesnel: ${tpesnel}")
        tpesnel.head
    }

    defn
  }


}

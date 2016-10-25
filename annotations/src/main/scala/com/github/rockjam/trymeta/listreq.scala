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
import scala.annotation.StaticAnnotation

class listreq extends StaticAnnotation {

  inline def apply(defn: Any): Any = meta {
    val q"object $name { ..${stats: Seq[Stat]} }" = defn

    val requests = stats collect {
      case c: Defn.Class => c
    }

    val jsonFormatters = {
      val importCirce = q"import io.circe._, io.circe.generic.semiauto._"

      val encodersDecoders = requests flatMap { req =>
        val reqName = req.name.value
        val nameLowerized = reqName.headOption map (_.toLower + reqName.tail) getOrElse reqName
        val encoderName = Pat.Var.Term(Term.Name(s"${nameLowerized}Encoder"))
        val decoderName = Pat.Var.Term(Term.Name(s"${nameLowerized}Decoder"))
        val typeName = Type.Name(reqName)

        Seq(
          q"implicit val $encoderName: Encoder[$typeName] = deriveEncoder[$typeName]",
          q"implicit val $decoderName: Decoder[$typeName] = deriveDecoder[$typeName]"
        )
      }
      q"object JsonFormatters { $importCirce; ..$encodersDecoders }"
    }

    val allRequests = {
      val elems = requests map (e => "\"" + e.name.value + "\"") mkString ", "
      q"""val allRequests: List[String] =  List($elems)"""
    }

    val allShit = {
      val elems = stats map (_.toString()) mkString("\"", " ", "\"")
      q"val allShit: String = $elems"
    }

    val result = q"object $name { ..$stats; $allRequests; $allShit; $jsonFormatters }"
    println(s"===result: ${result}")
    result
  }

}

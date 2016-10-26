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
    def lowerize(s: String) = s.headOption map (_.toLower + s.tail) getOrElse s

    def aux(defn: Tree) = {
      val q"object $name { ..${stats: Seq[Stat]} }" = defn

      val jsonFormatters = {
        val reqNames: Seq[String] = stats collect {
          case c: Defn.Class => c.name.value
        }

        val importCirce = q"import io.circe._, io.circe.syntax._, io.circe.generic.semiauto._, io.circe.Decoder.Result"
        val importCats = q"import cats.data.Xor"

        val encoders = reqNames map { reqName =>
          val nameLowerized = lowerize(reqName)
          val encoderName = Pat.Var.Term(Term.Name(s"${nameLowerized}Encoder"))
          val typeName = Type.Name(reqName)

          q"""
          implicit val $encoderName: Encoder[$typeName] = new Encoder[$typeName] {
            def apply(a: $typeName): Json =
              JsonObject.fromMap(Map(
                "method" -> Json.fromString($nameLowerized),
                "params" -> a.asJson(deriveEncoder[$typeName])
              )).asJson
          }
        """
        }

        val decoder = {

          val baseTraits = stats collect {
            case t: Defn.Trait => t.name.value
          }
          require(baseTraits.length == 1, "There should be only one base trait for requests")

          val cases = (reqNames map { name =>
            val typeName = Type.Name(name)
            val nameLowerized = lowerize(name)

            p"""case Xor.Right($nameLowerized) => c.downField("params").as[$typeName](deriveDecoder[$typeName])"""
          }) :+ p"""case _ => Xor.Left(DecodingFailure("method is not present", c.downField("method").history))"""

          val (decoderName, baseType) = {
            val requestBaseName = baseTraits.head
            val nameLowerized = lowerize(requestBaseName)
            Pat.Var.Term(Term.Name(nameLowerized)) -> Type.Name(requestBaseName)
          }

          q"""
            implicit val $decoderName: Decoder[$baseType] = new Decoder[$baseType] {
              def apply(c: HCursor): Result[$baseType] = {
               c.downField("method").as[String] match {
                 ..case$cases
               }
              }
            }
          """
        }

        q"object JsonFormatters { $importCirce; $importCats; ..$encoders; $decoder }"
      }

      val result = q"object $name { ..$stats; $jsonFormatters }"
//      println(s"\n $result \n")
      result
    }

    aux(defn)
  }



}

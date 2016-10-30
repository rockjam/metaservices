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

import scala.annotation.StaticAnnotation
import scala.meta._

class generatedJsonrpc extends StaticAnnotation {

  inline def apply(defn: Any): Any = meta {

    println("============ in jsonrpc")

    def aux(defn: Tree) = {
      val q"..$mods object $name { ..${stats: Seq[Stat]} }" = defn
      val objectName = name.value

      val jsonRpc = {
        val serviceRequests: Seq[MethodDescription] = ServiceCommon.extractServiceRequests(stats)

        val handleRequest = {
          val handleCases =  serviceRequests map { case MethodDescription(reqType, respType, paramss) =>
            val reqString = reqType.syntax

            val func = {
              val requestNameLowerized = Utils.lowerize(reqString)

              val paramName = param"${Term.Name(requestNameLowerized)}"
              val methodName = Term.Name(s"handle$reqType")
              val fieldSelects = paramss.flatten map (_.name.value) map { n =>
                val objectTermName = Term.Name(requestNameLowerized)
                val fieldTermName = Term.Name(n)
                arg"$objectTermName.$fieldTermName"
              }
              val errorHandle = {
                val casesnel = List(
                  p"case Xor.Right(res) => Xor.Right(res.asJson)",
                  p"case Xor.Left(err) => Xor.Left(JsonRpcError(err.code, err.message, None))" //TODO: provide err.data conversion
                )
                q"{ ..case $casesnel }"
              }

              q"($paramName) => service.$methodName(..$fieldSelects).map($errorHandle) "
            }

            val jsonRpcMethodName = s"${objectName}.${reqString}"

            p"""
              case $jsonRpcMethodName =>
               json
                 .as[$reqType]
                 .map($func)
                 .getOrElse(Future.successful(Xor.Left(JsonRpcErrors.InvalidParams)))
            """
          }

          q"""
            def handleRequest: Json => PartialFunction[String, Future[Xor[JsonRpcError, Json]]] = json => { ..case $handleCases }
          """
        }

        val imports = {
          val i = List(
            importer"com.github.rockjam.metaservices.jsonrpc._",
            importer"cats.data.Xor",
            importer"scala.concurrent.Future",
            importer"io.circe._",
            importer"io.circe.syntax._",
            importer"io.circe.generic.auto._"
          )
          q"import ..$i"
        }

        val jsonrpcName = Type.Name(objectName + "JsonRpc")
        val jsonrpcBaseType = ctor"com.github.rockjam.metaservices.jsonrpc.JsonRpcService"
        val serviceName = Type.Name(objectName + "Service")

        q"""
          final class $jsonrpcName(service: $serviceName)(implicit ec: scala.concurrent.ExecutionContext) extends $jsonrpcBaseType {
           $imports
           $handleRequest
          }
        """
      }

      Utils.logResult(q"..$mods object $name { ..$stats; $jsonRpc }")
    }

    aux(defn)

  }

}


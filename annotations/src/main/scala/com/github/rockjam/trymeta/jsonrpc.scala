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

class jsonrpc extends StaticAnnotation {

  inline def apply(defn: Any): Any = meta {

    println("============ in jsonrpc")
    def aux(defn: Tree) = {
      val q"..$mods object $name { ..${stats: Seq[Stat]} }" = defn

      val objectName = name.value

      val imports = {
        val i = Seq(
          importer"com.github.rockjam.trymeta.jsonrpc20._",
          importer"cats.data.Xor",
          importer"scala.concurrent.Future",
          importer"io.circe._",
          importer"io.circe.syntax._",
          importer"io.circe.generic.semiauto._"
        )
        q"import ..$i"
      }

      val rpcRequests: Seq[MethodDescription] = ServiceCommon.extractRpcRequests(stats)

      println(s"===rpc requests: ${rpcRequests}")

      val handleCases =  rpcRequests map { case MethodDescription(reqType, respType, paramss) =>
        val reqString = reqType.syntax
        val caseName = s"${objectName}.${reqString}"

        println(s"=====case name is: ${caseName}")


        val methodName = Term.Name(s"handle$reqType")


        val lowerizedName = Utils.lowerize(reqString)
        val name = Term.Name(lowerizedName)



        // paramStrings
        val paramsNames = paramss.flatten map (_.name.value)
        val calls = paramsNames map { n =>
          val objectTermName = Term.Name(lowerizedName)
          val fieldTermName = Term.Name(n)
          arg"$objectTermName.$fieldTermName"
        }


        val errorHandle = {
          val casesnel = Seq(
            p"case Xor.Right(res) => Xor.Right(res.asJson(deriveEncoder[$respType]))",
            p"case Xor.Left(err) => Xor.Left(JsonRpcError(err.code, err.message, None))" //TODO: provide err.data conversion
          )
          q"{ ..case $casesnel }"
        }

        val paramName = param"$name"
        val func = q"($paramName) => service.$methodName(..$calls).map($errorHandle) "


        p"""case $caseName =>
           json
            .as[$reqType](deriveDecoder[$reqType])
            .map($func)
            .getOrElse(Future.successful(Xor.Left(JsonRpcErrors.InvalidParams)))
        """
      }

      val pf = q"{ ..case $handleCases }"

      val handleRequest = q"""
        def handleRequest: Json => PartialFunction[String, Future[Xor[JsonRpcError, Json]]] = json => $pf
      """

      val jsonrpcName = Type.Name(objectName + "JsonRpc")
      val jsonrpcBaseType = ctor"com.github.rockjam.trymeta.jsonrpc20.JsonRpcService"

      val serviceName = Type.Name(objectName + "Service")

      val jsonRpc = q"""
        final class $jsonrpcName(service: $serviceName)(implicit ec: scala.concurrent.ExecutionContext) extends $jsonrpcBaseType {
         $imports
         $handleRequest
        }
      """

      val result = q"..$mods object $name { ..$stats; $jsonRpc }"
      println(s"\n $result \n")
      result
    }

    aux(defn)

  }

}


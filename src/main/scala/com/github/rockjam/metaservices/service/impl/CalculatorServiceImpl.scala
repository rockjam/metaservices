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
package impl

import cats.syntax.xor._
import models.Calculator._

import scala.concurrent.Future

class CalculatorServiceImpl extends CalculatorService {
  def handleAdd(a: Option[Int], b: Option[Int]): Future[Result[IntegerResponse]] = {
    val result = (for {
      av <- a
      bv <- b
    } yield IntegerResponse(Some(av + bv)).right) getOrElse ServiceErrors.ValidationError.left
    Future.successful(result)
  }

  def handleDevide(a: Option[Int], b: Option[Int]): Future[Result[DoubleResponse]] = {
    val result = (for {
      av <- a
      bv <- b
    } yield
        DoubleResponse(Some(av.toDouble / bv.toDouble)).right) getOrElse ServiceErrors.ValidationError.left
    Future.successful(result)
  }

  def handleMultiply(a: Option[Int], b: Option[Int]): Future[Result[IntegerResponse]] = {
    val result = (for {
      av <- a
      bv <- b
    } yield IntegerResponse(Some(av * bv)).right) getOrElse ServiceErrors.ValidationError.left
    Future.successful(result)
  }

  def handleSubtract(a: Option[Int], b: Option[Int]): Future[Result[IntegerResponse]] = {
    val result = (for {
      av <- a
      bv <- b
    } yield IntegerResponse(Some(av - bv)).right) getOrElse ServiceErrors.ValidationError.left
    Future.successful(result)
  }

}

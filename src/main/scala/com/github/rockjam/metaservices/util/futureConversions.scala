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

package com.github.rockjam.metaservices.util

import com.twitter.util.{ Future ⇒ TFuture, Promise ⇒ TPromise, Return, Throw }
import scala.concurrent.{ Future ⇒ SFuture, Promise ⇒ SPromise, ExecutionContext }
import scala.util.{ Failure, Success }

object futureConversions {
  implicit class RichTFuture[A](val f: TFuture[A]) extends AnyVal {
    def asScala(implicit e: ExecutionContext): SFuture[A] = {
      val p: SPromise[A] = SPromise()
      f.respond {
        case Return(value)    ⇒ p.success(value)
        case Throw(exception) ⇒ p.failure(exception)
      }

      p.future
    }
  }

  implicit class RichSFuture[A](val f: SFuture[A]) extends AnyVal {
    def asTwitter(implicit e: ExecutionContext): TFuture[A] = {
      val p: TPromise[A] = new TPromise[A]
      f.onComplete {
        case Success(value)     ⇒ p.setValue(value)
        case Failure(exception) ⇒ p.setException(exception)
      }

      p
    }
  }
}

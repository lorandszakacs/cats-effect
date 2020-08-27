/*
 * Copyright 2020 Typelevel
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

package cats.effect

import cats.{Eq, Monad, Show}
import cats.data.ReaderWriterStateT
//import cats.laws.discipline.{AlignTests, ParallelTests}
import cats.laws.discipline.arbitrary._
import cats.laws.discipline.eq._
import cats.laws.discipline.MiniInt
import cats.syntax.all._
//import cats.effect.kernel.ParallelF
import cats.effect.laws.MonadCancelTests
import cats.effect.testkit.{pure, PureConcGenerators}, pure._

// import org.scalacheck.rng.Seed
import org.scalacheck.util.Pretty

import org.specs2.ScalaCheck
import org.specs2.scalacheck.Parameters
import org.specs2.mutable._

import org.typelevel.discipline.specs2.mutable.Discipline

class ReaderWriterStateTPureConcSpec extends Specification with Discipline with ScalaCheck {
  import PureConcGenerators._

  implicit def prettyFromShow[A: Show](a: A): Pretty =
    Pretty.prettyString(a.show)

  implicit def rwstEq[F[_]: Monad, E, L, S, A](
      implicit ev: Eq[(E, S) => F[(L, S, A)]]): Eq[ReaderWriterStateT[F, E, L, S, A]] =
    Eq.by[ReaderWriterStateT[F, E, L, S, A], (E, S) => F[(L, S, A)]](_.run)

  checkAll(
    "ReaderWriterStateT[PureConc]",
    MonadCancelTests[ReaderWriterStateT[PureConc[Int, *], MiniInt, Int, MiniInt, *], Int]
      .monadCancel[Int, Int, Int]
    // we need to bound this a little tighter because these tests take FOREVER, especially on scalajs
  )(Parameters(minTestsOk =
    1 /*, seed = Some(Seed.fromBase64("IDF0zP9Be_vlUEA4wfnKjd8gE8RNQ6tj-BvSVAUp86J=").get*/ ))
}
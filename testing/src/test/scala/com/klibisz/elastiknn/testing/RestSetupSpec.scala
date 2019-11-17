package com.klibisz.elastiknn.testing

import com.klibisz.elastiknn.Distance
import com.klibisz.elastiknn.elastic4s._
import com.sksamuel.elastic4s.Executor
import org.scalatest.concurrent.AsyncTimeLimitedTests
import org.scalatest.time.Span
import org.scalatest.{AsyncFunSpec, AsyncFunSuite, Inspectors, Matchers}

import scala.concurrent.Future
import scala.concurrent.duration._

class RestSetupSpec extends AsyncFunSuite with AsyncTimeLimitedTests with Matchers with Inspectors with ElasticAsyncClient {

  override def timeLimit: Span = 10.seconds

  test("hits the setup endpoint") {
    for {
      setupRes <- client.execute(ElastiKnnSetupRequest())
    } yield {
      setupRes.isSuccess shouldBe true
    }
  }

  test("installs stored scripts") {
    val distances: Seq[String] = Distance.values.tail.map(_.name.toLowerCase.replace("distance_", ""))

    for {
      setupRes <- client.execute(ElastiKnnSetupRequest())
      getScriptRequests = distances.map(d => client.execute(GetScriptRequest(s"elastiknn-exact-$d")))
      getScriptResults <- Future.sequence(getScriptRequests)
    } yield {
      setupRes.isSuccess shouldBe true
      forAll(getScriptResults)(_.isSuccess shouldBe true)
    }
  }

}

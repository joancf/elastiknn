package com.klibisz.elastiknn.testing

import com.sksamuel.elastic4s.ElasticDsl._
import org.scalatest.{AsyncFunSuite, Matchers}

class ClusterSpec extends AsyncFunSuite with Matchers with ElasticAsyncClient {

  test("returns green health") {
    for {
      healthRes <- client.execute(catHealth())
    } yield {
      healthRes.isSuccess shouldBe true
      healthRes.result.status shouldBe "green"
    }
  }

  test("installed the plugin") {
    for {
      catPluginsRes <- client.execute(catPlugins())
    } yield {
      catPluginsRes.isSuccess shouldBe true
      catPluginsRes.result should not be empty
      catPluginsRes.result.head.component shouldBe "elastiknn"
    }
  }

  test("started four nodes") {
    for {
      catNodesRes <- client.execute(catNodes())
    } yield {
      catNodesRes.isSuccess shouldBe true
      catNodesRes.result should have length 4
      catNodesRes.result.map(_.nodeRole).sorted shouldBe Seq("-", "dil", "dil", "m").sorted
    }
  }

}

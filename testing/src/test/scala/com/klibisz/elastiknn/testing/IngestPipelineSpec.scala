package com.klibisz.elastiknn.testing

import com.klibisz.elastiknn.{ExactModelOptions, LshModelOptions, ProcessorOptions}
import com.klibisz.elastiknn.ProcessorOptions.ModelOptions
import com.klibisz.elastiknn.elastic4s.{Pipeline, PipelineRequest, Processor}
import org.scalatest.{AsyncFunSuite, Matchers}

class IngestPipelineSpec extends AsyncFunSuite with Matchers with ElasticAsyncClient {

  test("make an exact pipeline") {
    val opts = ProcessorOptions("a", "b", false, 32, ModelOptions.Exact(ExactModelOptions()))
    val req = PipelineRequest("exact", Pipeline("d", Seq(Processor("elastiknn", opts))))
    for {
      res <- client.execute(req)
    } yield {
      res.isSuccess shouldBe true
      res.result.acknowledged shouldBe true
    }
  }

  test("make an lsh pipeline") {
    val opts = ProcessorOptions("a", "b", false, 32, ModelOptions.Lsh(LshModelOptions(k = 10, l = 20)))
    val req = PipelineRequest("lsh", Pipeline("d", Seq(Processor("elastiknn", opts))))
    for {
      res <- client.execute(req)
    } yield {
      res.isSuccess shouldBe true
      res.result.acknowledged shouldBe true
    }
  }

}

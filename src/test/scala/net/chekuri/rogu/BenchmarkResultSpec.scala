package net.chekuri.rogu

import net.chekuri.rogu.utilities.{RoguBenchmark, RoguLogger}
import org.scalatest.flatspec.AnyFlatSpec

class BenchmarkResultSpec extends AnyFlatSpec with RoguLogger with RoguBenchmark {
  "nanosToMicro" should "correctly convert nanoseconds to microseconds" in {
    val payload: BenchmarkResult[Long] = BenchmarkResult(getThreadId(), BigInt.apply(1000))
    logger.info(s"Associated thread ID : ${payload.thread_id}")
    logger.info(s"Associated thread Name : ${payload.thread_name}")
    logger.info(s"Time taken to execute task (ns): ${payload.nanos}")
    logger.info(s"Time taken to execute task (μs): ${payload.nanosToMicro}")
    logger.info(s"Result: ${payload.result}")
    assert(payload.nanosToMicro == 1)
  }

  "nanosToMilli" should "correctly convert nanoseconds to milliseconds" in {
    val payload: BenchmarkResult[Long] = BenchmarkResult(getThreadId(), BigInt.apply(1000000))
    logger.info(s"Associated thread ID : ${payload.thread_id}")
    logger.info(s"Associated thread Name : ${payload.thread_name}")
    logger.info(s"Time taken to execute task (ns): ${payload.nanos}")
    logger.info(s"Time taken to execute task (ms): ${payload.nanosToMilli}")
    logger.info(s"Result: ${payload.result}")
    assert(payload.nanosToMilli == 1)
  }

  "nanosToSecs" should "correctly convert nanoseconds to seconds" in {
    val payload: BenchmarkResult[Long] = BenchmarkResult(getThreadId(), BigInt.apply(1000000000))
    logger.info(s"Associated thread ID : ${payload.thread_id}")
    logger.info(s"Associated thread Name : ${payload.thread_name}")
    logger.info(s"Time taken to execute task (ns): ${payload.nanos}")
    logger.info(s"Time taken to execute task (s): ${payload.nanosToSecs}")
    logger.info(s"Result: ${payload.result}")
    assert(payload.nanosToSecs == 1)
  }

  "nanosToMins" should "correctly convert nanoseconds to minutes" in {
    val payload: BenchmarkResult[Long] = BenchmarkResult(getThreadId(), BigInt.apply("60000000000"))
    logger.info(s"Associated thread ID : ${payload.thread_id}")
    logger.info(s"Associated thread Name : ${payload.thread_name}")
    logger.info(s"Time taken to execute task (ns) : ${payload.nanos}")
    logger.info(s"Time taken to execute task (min): ${payload.nanosToMins}")
    logger.info(s"Result: ${payload.result}")
    assert(payload.nanosToMins == 1)
  }
}

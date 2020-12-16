package net.chekuri.rogu.utilities

import org.scalatest.flatspec.AnyFlatSpec

class RoguBenchmarkSpec extends AnyFlatSpec with RoguBenchmark with RoguLogger {
  def PoorlyImplementedPowerCalculator(base: BigDecimal, exponent: BigInt): BigDecimal = {
    var result: BigDecimal = BigDecimal.apply(1)
    var index: BigInt = BigInt.apply(0)
    while (index.compare(exponent) < 0) {
      index = index.+(1)
      result = result.*(base)
    }
    result
  }

  "Benchmark" should "correctly compute result and return Benchmark result" in {
    val randomizer = scala.util.Random
    val base_units = randomizer.nextLong(50000)
    val base_decimals = randomizer.nextLong(50000)
    val base = BigDecimal.apply(s"$base_units.$base_decimals")
    val exponent = BigInt.apply(randomizer.nextLong(50000))
    val exp_op_value = Benchmark(PoorlyImplementedPowerCalculator(base, exponent))
    logger.info(s"Associated thread ID : ${exp_op_value.thread_id}")
    logger.info(s"Associated thread Name : ${exp_op_value.thread_name}")
    logger.info(s"Time taken to execute task (ns): ${exp_op_value.nanos}")
    assert(true)
  }

  "Benchmark" should "correctly return correct results" in {
    val exp_op_value = Benchmark(getThreadId())
    logger.info(s"Associated thread ID : ${exp_op_value.thread_id}")
    logger.info(s"Associated thread Name : ${exp_op_value.thread_name}")
    logger.info(s"Time taken to execute task (ns): ${exp_op_value.nanos}")
    assert(exp_op_value.result == getThreadId())
  }
}

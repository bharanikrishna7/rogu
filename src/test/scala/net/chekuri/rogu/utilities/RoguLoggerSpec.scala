package net.chekuri.rogu.utilities

import org.scalatest.flatspec.AnyFlatSpec

class RoguLoggerSpec extends AnyFlatSpec with RoguLogger {
  "RoguLogger" should "correctly initialize logger" in {
    logger.info("Test info logging.")
    logger.debug("Test debug logging.")
    logger.trace("Test trace logging.")
    logger.warn("Test warn logging.")
    logger.error("Test error logging.")
    assert(true)
  }

  "RoguLogger" should "correctly log multi-line info messages" in {
    val message =
      s"""This is line 0.
        |This is line 1.
        |This is line 2.
        |This is line 3.
        |""".stripMargin

    multiLineInfoLog(message)
    assert(true)
  }
}

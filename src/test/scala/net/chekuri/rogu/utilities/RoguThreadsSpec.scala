package net.chekuri.rogu.utilities

import org.scalatest.flatspec.AnyFlatSpec

class RoguThreadsSpec extends AnyFlatSpec with RoguThreads with RoguLogger {
  "getThreadId" should "correctly retrieve current thread id" in {
    val current_thread_id: Long = getThreadId()
    logger.info(s"Current Thread ID: $current_thread_id")
    assert(current_thread_id > 0)
  }

  "getThreadName" should "correctly retrieve current thread name" in {
    val current_thread_name = getThreadName()
    logger.info(s"Current Thread Name: $current_thread_name")
    assert(current_thread_name.contains("RoguThreadsSpec"))
  }

  "setThreadName" should "correctly retrieve set thread name" in {
    val current_thread_name = getThreadName()
    logger.info(s"Current Thread Name: $current_thread_name")
    val new_thread_name = "RoguThreadsSpec-setThreadName-thread"
    logger.debug("Updating current thread name.")
    setThreadName(new_thread_name)
    logger.debug("Updated current thread name.")
    logger.info(s"New Thread Name: ${getThreadName()}")
    assert(new_thread_name.equals(getThreadName()))
  }
}

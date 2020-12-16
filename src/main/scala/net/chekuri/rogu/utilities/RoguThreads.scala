package net.chekuri.rogu.utilities

/** Interface to provide convenient methods
  * to retrieve and set thread level information.
  */
trait RoguThreads extends RoguLogger {
  logger.trace(s"Spawned new thread with id: ${getThreadId()} \tname: ${getThreadName()}")
  /** Method to get current thread id.
    *
    * @return current thread id.
    */
  def getThreadId(): Long = {
    Thread.currentThread().getId
  }

  /** Method to return current thread name.
    *
    * @return current thread name.
    */
  def getThreadName(): String = {
    Thread.currentThread().getName
  }

  /** Method to set current thread name.
    *
    * @param name name to set.
    * @return true if thread name is updated, false otherwise.
    */
  def setThreadName(name: String): Boolean = {
    try {
      Thread.currentThread().setName(name)
      true
    } catch {
      case ex: Exception =>
        false
    }
  }
}

package net.chekuri.rogu.utilities

import com.typesafe.scalalogging.LazyLogging

/** Interface to provide convenient methods
  * for logging.
  */
trait RoguLogger extends LazyLogging {
  /** Helper method to split string to lines.
    *
    * @param message message to split into lines.
    * @return array of strings separated by '\n'.
    */
  protected def splitStringToLines(message: String): Array[String] = {
    message.split('\n')
  }

  /** Helper method to perform multi-line logging.
    *
    * @param message message to log.
    * @param mode    Logging mode. it can be one of the following:
    *                * trace
    *                * debug
    *                * info
    *                * warn
    *                * error
    */
  private def multiLineLogHelper(message: String, mode: String): Unit = {
    val split_payload = splitStringToLines(message)
    mode match {
      case "trace" => split_payload.map(x => logger.trace(x))
      case "debug" => split_payload.map(x => logger.debug(x))
      case "warn" => split_payload.map(x => logger.warn(x))
      case "error" => split_payload.map(x => logger.error(x))
      case _ => split_payload.map(x => logger.info(x))
    }
  }

  /** Method to perform multi-line trace logging.
    *
    * @param message message to log.
    */
  def multiLineTraceLog(message: String): Unit = {
    multiLineLogHelper(message, "trace")
  }

  /** Method to perform multi-line debug logging.
    *
    * @param message message to log.
    */
  def multiLineDebugLog(message: String): Unit = {
    multiLineLogHelper(message, "debug")
  }

  /** Method to perform multi-line info logging.
    *
    * @param message message to log.
    */
  def multiLineInfoLog(message: String): Unit = {
    multiLineLogHelper(message, "info")
  }

  /** Method to perform multi-line warn logging.
    *
    * @param message message to log.
    */
  def multiLineWarnLog(message: String): Unit = {
    multiLineLogHelper(message, "warn")
  }

  /** Method to perform multi-line error logging.
    *
    * @param message message to log.
    */
  def multiLineErrorLog(message: String): Unit = {
    multiLineLogHelper(message, "error")
  }
}

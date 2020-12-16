package net.chekuri.rogu.jdbc

import net.chekuri.rogu.utilities.RoguLogger

import java.sql.{Connection, DriverManager}
import java.util.Properties

object MySqlDatabaseConnector extends RoguLogger {
  private val host: String = "localhost"
  private val port: Int = 3306
  private val database: String = "employees"
  val driver: String = "com.mysql.jdbc.Driver"
  private val username: String = "root"

  def generateConnection: Connection = {
    try {
      Class.forName(driver)
      val url = s"jdbc:mysql://$host:$port/$database"
      val props = new Properties()
      props.put("user", username)
      props.put("useSSL", "true")
      DriverManager.getConnection(url, props)
    } catch {
      case ex: Exception =>
        logger.error("Encountered exception while generation mysql jdbc connection.")
        logger.error("Rethrowing caught exception.")
        throw ex
    }
  }
}

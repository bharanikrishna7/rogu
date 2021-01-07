package net.chekuri.rogu.jdbc

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import net.chekuri.rogu.utilities.RoguLogger

import java.sql.{Connection, DriverManager}
import java.util.Properties

object MySqlDatabaseConnector extends RoguLogger {
  private val host: String = "mysql-rfam-public.ebi.ac.uk"
  private val port: Int = 4497
  private val database: String = "Rfam"
  val driver: String = "com.mysql.jdbc.Driver"
  private val username: String = "rfamro"
  val url = s"jdbc:mysql://$host:$port/$database"

  def generateConnection: Connection = {
    try {
      Class.forName(driver)
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

  def generateConnectionPool: HikariDataSource = {
    val config: HikariConfig = new HikariConfig()
    config.setJdbcUrl(url)
    config.setUsername(username)
    config.addDataSourceProperty("useSSL", true)

    val data_source: HikariDataSource = new HikariDataSource(config)
    data_source
  }
}

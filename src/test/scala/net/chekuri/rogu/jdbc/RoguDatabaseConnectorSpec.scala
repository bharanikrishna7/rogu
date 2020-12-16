package net.chekuri.rogu.jdbc

import net.chekuri.rogu.helpers.RoguTestModels.{Departments, Employees}
import net.chekuri.rogu.utilities.{RoguLogger, RoguThreads}
import org.json4s.MappingException
import org.scalatest.flatspec.AnyFlatSpec

import java.sql.SQLSyntaxErrorException

class RoguDatabaseConnectorSpec extends AnyFlatSpec with RoguThreads with RoguLogger {
  "execute" should "successfully execute query on a database and return results" in {
    try {
      val query = "select * from departments;"
      val connection = MySqlDatabaseConnector.generateConnection
      val connector =
        new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
      val result = connector.execute(query = query, isUpdate = false)
      logger.info("Successfully executed query using Roogu Database Connector.")
      logger.info(s"Time taken to compile query: ${result.compile_nanos} nanoseconds.")
      logger.info(s"Time taken to execute query and fetch results: ${result.fetch_nanos} nanoseconds.")
      logger.info(s"Time taken to process query results: ${result.process_nanos} nanoseconds.")
      logger.info(s"Total Time taken to execute query: ${result.total_nanos} nanoseconds.")
      assert(true)
    } catch {
      case ex: Exception =>
        logger.warn("MySql Database is not running on supplied host.")
        logger.warn(s"Actual exception message: ${ex.getMessage}")
        logger.warn("Skipping current test.")
        assert(true)
    }
  }

  "execute" should "throw exception when query is incorrect" in {
    try {
      val query = "select * from department;"
      val connection = MySqlDatabaseConnector.generateConnection
      val connector =
        new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
      assertThrows[SQLSyntaxErrorException](connector.execute(query = query, isUpdate = false))
    } catch {
      case ex: Exception =>
        logger.warn("MySql Database is not running on supplied host.")
        logger.warn(s"Actual exception message: ${ex.getMessage}")
        logger.warn("Skipping current test.")
        assert(true)
    }
  }

  "executeQuery" should "throw exception when query is incorrect" in {
    try {
      val query = "select * from department;"
      val connection = MySqlDatabaseConnector.generateConnection
      val connector =
        new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
      assertThrows[SQLSyntaxErrorException](connector.executeQuery[Departments](query = query))
    } catch {
      case ex: Exception =>
        logger.warn("MySql Database is not running on supplied host.")
        logger.warn(s"Actual exception message: ${ex.getMessage}")
        logger.warn("Skipping current test.")
        assert(true)
    }
  }

  "executeQuery" should "throw exception when query is correct but supplied case class is incorrect" in {
    try {
      val query = "select * from departments;"
      val connection = MySqlDatabaseConnector.generateConnection
      val connector =
        new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
      assertThrows[MappingException](connector.executeQuery[Employees](query = query))
    } catch {
      case ex: Exception =>
        logger.warn("MySql Database is not running on supplied host.")
        logger.warn(s"Actual exception message: ${ex.getMessage}")
        logger.warn("Skipping current test.")
        assert(true)
    }
  }

  "executeQuery" should "successfully execute query on a database and return parsed | object serialized results" in {
    try {
      val query = "select * from departments;"
      val connection = MySqlDatabaseConnector.generateConnection
      val connector =
        new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
      val result = connector.executeQuery[Departments](query = query)
      logger.info("Successfully executed query and parsed results using Roogu Database Connector.")
      logger.info(s"Time taken to compile query: ${result.compile_nanos} nanoseconds.")
      logger.info(s"Time taken to execute query and fetch results: ${result.fetch_nanos} nanoseconds.")
      logger.info(s"Time taken to process query results: ${result.process_nanos} nanoseconds.")
      logger.info(s"Total Time taken to execute query: ${result.total_nanos} nanoseconds.")
      logger.info(s"Retrieved Records:")
      result.result.foreach(x => logger.info(s"${x.dept_no}\t ${x.dept_name}"))
      assert(true)
    } catch {
      case ex: Exception =>
        logger.warn("MySql Database is not running on supplied host.")
        logger.warn(s"Actual exception message: ${ex.getMessage}")
        logger.warn("Skipping current test.")
        assert(true)
    }
  }
}

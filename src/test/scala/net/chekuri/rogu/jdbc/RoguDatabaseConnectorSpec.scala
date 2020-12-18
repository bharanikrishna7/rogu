package net.chekuri.rogu.jdbc

import net.chekuri.rogu.helpers.RoguTestModels.{
  Author,
  Departments,
  Employees,
  FamilyAuthor,
  FamilyAuthorBoolean,
  Version
}
import net.chekuri.rogu.parser.RoguParser
import net.chekuri.rogu.utilities.RoguLogger
import org.json4s.MappingException
import org.scalatest.flatspec.AnyFlatSpec

import java.sql.SQLSyntaxErrorException

class RoguDatabaseConnectorSpec extends AnyFlatSpec with RoguParser with RoguLogger {
  "getId" should "successfully retrieve the ID assigned to the RoguDatabaseConnector instance" in {
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())

    logger.info("Successfully initialized Rogu Database Connector.")
    assert(getThreadId() == connector.getThreadId())
  }

  "execute" should "successfully execute query on a database and return results" in {
    val query = "SELECT * FROM author LIMIT 5;"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    val result = connector.execute(query = query, isUpdate = false)
    logger.info("Successfully executed query using Roogu Database Connector.")
    logger.info(s"Time taken to compile query: ${result.compile_nanos} nanoseconds.")
    logger.info(s"Time taken to execute query and fetch results: ${result.fetch_nanos} nanoseconds.")
    logger.info(s"Time taken to process query results: ${result.process_nanos} nanoseconds.")
    logger.info(s"Total Time taken to execute query: ${result.total_nanos} nanoseconds.")
    connection.close()
    assert(true)

  }

  "execute" should "throw exception when query is incorrect" in {
    val query = "SELECT * FROM authors LIMIT 5;"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    assertThrows[SQLSyntaxErrorException]({
      try {
        connector.execute(query = query, isUpdate = false)
      } catch {
        case ex: Exception =>
          connection.close()
          logger.error(s"Actual Exception Message: ${ex.getMessage}")
          throw ex
      }
    })

  }

  "executeQuery" should "throw exception when query is incorrect" in {
    val query = "select * from department;"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    assertThrows[SQLSyntaxErrorException]({
      try {
        connector.executeQuery[Departments](query = query)
      } catch {
        case ex: Exception =>
          connection.close()
          logger.error(s"Actual Exception Message: ${ex.getMessage}")
          throw ex
      }
    })

  }

  "executeQuery" should "throw exception when query is correct but supplied case class is incorrect" in {
    val query = "SELECT * FROM author LIMIT 5;"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    assertThrows[MappingException]({
      try {
        connector.executeQuery[Employees](query = query)
      } catch {
        case ex: Exception =>
          connection.close()
          logger.error(s"Actual Exception Message: ${ex.getMessage}")
          throw ex
      }
    })
  }

  "executeQuery" should "successfully execute query on a database and return parsed | object serialized results" in {
    val query = "SELECT * FROM author LIMIT 5;"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    val result = connector.executeQuery[Author](query = query)
    logger.info("Successfully executed query and parsed results using Roogu Database Connector.")
    logger.info(s"Time taken to compile query: ${result.compile_nanos} nanoseconds.")
    logger.info(s"Time taken to execute query and fetch results: ${result.fetch_nanos} nanoseconds.")
    logger.info(s"Time taken to process query results: ${result.process_nanos} nanoseconds.")
    logger.info(s"Total Time taken to execute query: ${result.total_nanos} nanoseconds.")
    logger.info(s"Retrieved Records:")
    logger.info(ObjectToJson[List[Author]](result.result, true))
    connection.close()
    assert(true)
  }

  "executeQuery" should "successfully execute query on a database and return parsed | object serialized results when some fields include timestamp" in {
    val query = "SELECT * FROM version LIMIT 5;"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    val result = connector.executeQuery[Version](query = query)
    logger.info("Successfully executed query and parsed results using Roogu Database Connector.")
    logger.info(s"Time taken to compile query: ${result.compile_nanos} nanoseconds.")
    logger.info(s"Time taken to execute query and fetch results: ${result.fetch_nanos} nanoseconds.")
    logger.info(s"Time taken to process query results: ${result.process_nanos} nanoseconds.")
    logger.info(s"Total Time taken to execute query: ${result.total_nanos} nanoseconds.")
    logger.info(s"Retrieved Records:")
    logger.info(ObjectToJson[List[Version]](result.result))
    connection.close()
    assert(true)
  }

  "executeQuery" should "successfully execute query on a database and return parsed | object serialized results even when boolean is derived from int(0, 1)" in {
    val query = "SELECT * FROM family_author WHERE desc_order IN (0, 1) LIMIT 10;"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    val result = connector.executeQuery[FamilyAuthorBoolean](query = query)
    logger.info("Successfully executed query and parsed results using Roogu Database Connector.")
    logger.info(s"Time taken to compile query: ${result.compile_nanos} nanoseconds.")
    logger.info(s"Time taken to execute query and fetch results: ${result.fetch_nanos} nanoseconds.")
    logger.info(s"Time taken to process query results: ${result.process_nanos} nanoseconds.")
    logger.info(s"Total Time taken to execute query: ${result.total_nanos} nanoseconds.")
    logger.info(s"Retrieved Records:")
    logger.info(ObjectToJson[List[FamilyAuthorBoolean]](result.result))
    connection.close()
    assert(true)
  }

  "executeQuery" should "successfully execute query on a database and return parsed | object serialized results as int when case class demands result as int instead of boolean" in {
    val query = "SELECT * FROM family_author WHERE desc_order IN (0, 1) LIMIT 10;"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    val result = connector.executeQuery[FamilyAuthor](query = query)
    logger.info("Successfully executed query and parsed results using Roogu Database Connector.")
    logger.info(s"Time taken to compile query: ${result.compile_nanos} nanoseconds.")
    logger.info(s"Time taken to execute query and fetch results: ${result.fetch_nanos} nanoseconds.")
    logger.info(s"Time taken to process query results: ${result.process_nanos} nanoseconds.")
    logger.info(s"Total Time taken to execute query: ${result.total_nanos} nanoseconds.")
    logger.info(s"Retrieved Records:")
    logger.info(ObjectToJson[List[FamilyAuthor]](result.result))
    connection.close()
    assert(true)
  }

  "execute" should "correctly execute valid update type query" in {
    val query = "SET time_zone = '-8:00';"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    val result = connector.execute(query, isUpdate = true)
    logger.info("Successfully executed query and parsed results using Roogu Database Connector.")
    logger.info(s"Time taken to compile query: ${result.compile_nanos} nanoseconds.")
    logger.info(s"Time taken to execute query and fetch results: ${result.fetch_nanos} nanoseconds.")
    logger.info(s"Time taken to process query results: ${result.process_nanos} nanoseconds.")
    logger.info(s"Total Time taken to execute query: ${result.total_nanos} nanoseconds.")
    logger.info(s"Return Value : ${result.records.values.last.toString.toInt}")
    connection.close()
    assert("effected_records | executed_operation_result" == result.columns.last.name)
    assert(BigInt.apply(1) == result.records.size)
  }

  "execute" should "correctly throw exception when supplied query is incorrect" in {
    val query = "SET time_zone = '-80000:0000000';"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())

    assertThrows[java.sql.SQLException]({
      try {
        connector.execute(query, isUpdate = true)
      } catch {
        case ex: Exception =>
          connection.close()
          logger.error(s"Actual Exception Message: ${ex.getMessage}")
          throw ex
      }
    })
  }
}

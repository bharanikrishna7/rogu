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
    assert(true)

  }

  "execute" should "throw exception when query is incorrect" in {
    val query = "SELECT * FROM authors LIMIT 5;"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    assertThrows[SQLSyntaxErrorException](connector.execute(query = query, isUpdate = false))

  }

  "executeQuery" should "throw exception when query is incorrect" in {
    val query = "select * from department;"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    assertThrows[SQLSyntaxErrorException](connector.executeQuery[Departments](query = query))

  }

  "executeQuery" should "throw exception when query is correct but supplied case class is incorrect" in {
    val query = "SELECT * FROM author LIMIT 5;"
    val connection = MySqlDatabaseConnector.generateConnection
    val connector =
      new RoguDatabaseConnector(connection = connection, driver = MySqlDatabaseConnector.driver, id = getThreadId())
    assertThrows[MappingException](connector.executeQuery[Employees](query = query))

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
    assert(true)
  }
}

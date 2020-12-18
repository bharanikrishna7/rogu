package net.chekuri.rogu.jdbc

import net.chekuri.rogu.jdbc.RoguDatabaseModels.{
  RoguDatabaseColumn,
  RoguDatabaseRecords,
  RoguDatabaseResult,
  RoguParsedDatabaseResult
}
import net.chekuri.rogu.utilities.{RoguBenchmark, RoguLogger, RoguThreads}

import java.sql.{Connection, ResultSet, ResultSetMetaData}

/** Rogu Database Connector provides an easy interface for connection
  * pools and jdbc connections to easily query and retrieve query execution
  * results.
  *
  * @param connection associated java sql connection
  * @param id         id to help identify instance when using along with thread pools.
  */
class RoguDatabaseConnector(connection: Connection, driver: String, id: Long)
  extends RoguRecordParser with RoguThreads with RoguLogger with RoguBenchmark {
  logger.trace(s"New Rogu Database Connector has been initialized on thread with ID: ${getThreadId()}")

  /** Method to return current rogu database connector
    * instance's id.
    *
    * @return id value supplied along with constructor.
    */
  def getId: Long = this.id

  /** Method to get JDBC Driver Name.
    *
    * @return jdbc driver name.
    */
  def getDriver: String = this.driver

  /** Method to execute a query using associated connection.
    *
    * @param query    query to execute.
    * @param isUpdate is update type query or operation.
    * @return Query execution results.
    */
  def execute(query: String, isUpdate: Boolean): RoguDatabaseResult = {
    val result = Benchmark {
      try {
        Class.forName(getDriver)
        logger.debug("preparing JDBC statement using supplied connection.")
        val create_statement = Benchmark(connection.createStatement())
        val statement = create_statement.result
        logger.debug("successfully prepared JDBC statement using supplied connection.")
        val jdbc_results = if (isUpdate) {
          logger.debug("executing update type query.")
          val execute = Benchmark(statement.executeUpdate(query))
          logger.debug("successfully executed update operation using jdbc connection.")
          logger.debug(s"time taken to execute update operation: ${execute.nanos} nanos")
          RoguDatabaseResult(
            columns = List[RoguDatabaseColumn](
              RoguDatabaseColumn("effected_records | executed_operation_result", "Int", Map[String, String]())),
            records = RoguDatabaseRecords(values = List[Any](execute.result), BigInt.apply(1)),
            compile_nanos = create_statement.nanos,
            fetch_nanos = execute.nanos,
            process_nanos = BigInt.apply(0)
          )
        } else {
          logger.debug("executing read type query.")
          val execute = Benchmark(statement.executeQuery(query))
          logger.debug("successfully executed read operation using jdbc connection.")
          logger.debug(s"time taken to execute read operation: ${execute.nanos} nanos")
          val resultset = execute.result
          val metadata = resultset.getMetaData
          val process = Benchmark {
            val columns = extractColumns(metadata)
            val records = extractRecords(resultset, columns)
            (columns, records)
          }
          RoguDatabaseResult(
            columns = process.result._1,
            records = process.result._2,
            compile_nanos = create_statement.nanos,
            fetch_nanos = execute.nanos,
            process_nanos = process.nanos
          )
        }
        jdbc_results
      } catch {
        case ex: Exception =>
          logger.error("Encountered exception while trying to execute query.")
          logger.error("Rethrowing caught exception.")
          throw ex
      }
    }
    val total_time = result.nanos
    val adjusted_result = result.result
    adjusted_result.updateTotalNanos(total_time)
    adjusted_result
  }

  /** Method to execute query and parse the results to class / case class
    * while extracting results.
    *
    * @param query query to execute.
    * @param m     Manifest associated with the class we want to parse to.
    * @tparam T Case Class / Class we want to parse.
    * @return Query Execution results converted into case class.
    */
  def executeQuery[T](query: String)(implicit m: Manifest[T]): RoguParsedDatabaseResult[T] = {
    val result = Benchmark {
      try {
        Class.forName(getDriver)
        logger.debug("preparing JDBC statement using supplied connection.")
        val create_statement = Benchmark(connection.createStatement())
        val statement = create_statement.result
        logger.debug("successfully prepared JDBC statement using supplied connection.")
        val execute = Benchmark(statement.executeQuery(query))
        logger.debug("successfully executed read operation using jdbc connection.")
        logger.debug(s"time taken to execute read operation: ${execute.nanos} nanos")
        val resultset = execute.result
        val metadata = resultset.getMetaData
        val process = Benchmark {
          val columns = extractColumns(metadata)
          val records: List[T] = parseAndExtractRecords[T](resultset, columns)
          (columns, records)
        }
        RoguParsedDatabaseResult(
          result = process.result._2,
          compile_nanos = create_statement.nanos,
          fetch_nanos = execute.nanos,
          process_nanos = process.nanos
        )
      } catch {
        case ex: Exception =>
          logger.error("Encountered exception while trying to execute query.")
          logger.error("Rethrowing caught exception.")
          throw ex
      }
    }
    val total_nanos = result.nanos
    val fin = result.result
    fin.updateTotalNanos(total_nanos)
    fin
  }

  /** Protected method to extract records using supplied result set and associated column set.
    *
    * @param resultset resultset returned by executeQuery statement.
    * @param columns   expected list of columns associated with resultset.
    * @return Records along with total count of records returned by resultset.
    */
  protected def extractRecords(resultset: ResultSet, columns: List[RoguDatabaseColumn]): RoguDatabaseRecords = {
    if (columns.isEmpty) {
      RoguDatabaseRecords(values = List[List[Any]](), size = BigInt.apply(0))
    } else {
      val col_count = columns.size
      var processed_results: List[List[Any]] = List[List[Any]]()
      var record_index: BigInt = BigInt.apply(0)
      while (resultset.next()) {
        record_index = record_index.+(1)
        val record_values: Array[Any] = new Array[Any](col_count)
        for (column_index <- 1 to col_count) {
          val column = columns(column_index - 1)
          record_values(column_index - 1) = resultset.getBytes(column.name)
        }
        processed_results = record_values.toList :: processed_results
      }
      RoguDatabaseRecords(processed_results, record_index)
    }
  }

  /** Protected Method to extract columns using supplied result set metadata.
    *
    * @param metadata result set metadata.
    * @return List of columns returned as part of result set.
    */
  protected def extractColumns(metadata: ResultSetMetaData): List[RoguDatabaseColumn] = {
    val col_count = metadata.getColumnCount
    val result: Array[RoguDatabaseColumn] = new Array[RoguDatabaseColumn](col_count)
    for (index <- 1 to col_count) {
      result(index - 1) =
        RoguDatabaseColumn(metadata.getColumnLabel(index), metadata.getColumnTypeName(index), Map[String, String]())
    }
    result.toList
  }
}

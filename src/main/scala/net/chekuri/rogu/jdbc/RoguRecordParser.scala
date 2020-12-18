package net.chekuri.rogu.jdbc

import net.chekuri.rogu.jdbc.RoguDatabaseModels.RoguDatabaseColumn
import net.chekuri.rogu.parser.RoguParser
import net.chekuri.rogu.utilities.RoguLogger

import java.sql.{Date, ResultSet, Timestamp}
import java.text.SimpleDateFormat

/** Interface to properly parse SQL Record fields
  * based on their data types.
  */
trait RoguRecordParser extends RoguParser with RoguLogger {
  /** SQL Defined String Data Types.
    */
  protected val string_data_types: Set[String] = Set[String](
    "CHAR",
    "VARCHAR",
    "BINARY",
    "VARBINARY",
    "TINYBLOB",
    "TINYTEXT",
    "TEXT",
    "BLOB",
    "MEDIUMTEXT",
    "MEDIUMBLOB",
    "LONGTEXT",
    "LONGBLOB"
  )

  /** SQL Defined Numeric Data Types.
    */
  protected val numeric_data_types: Set[String] = Set[String](
    "BIT",
    "TINYINT",
    "BOOL",
    "BOOLEAN",
    "SMALLINT",
    "MEDIUMINT",
    "INT",
    "INTEGER",
    "BIGINT",
    "FLOAT",
    "DOUBLE",
    "DECIMAL",
    "DEC",
    "YEAR"
  )

  /** SQL Defined Date Data Types.
    */
  protected val date_data_types: Set[String] = Set[String](
    "DATE",
    "DATETIME"
  )

  /** SQL Defined Timestamp Data Types.
    */
  protected val time_data_types: Set[String] = Set[String](
    "TIMESTAMP",
    "TIME"
  )

  /** Protected method to parse and extract records as list of
    * objects using the class manifest information.
    *
    * @param resultset resultset returned by executeQuery statement.
    * @param columns   expected list of columns associated with resultset.
    * @param m         Manifest associated with the class we want to parse to.
    * @tparam T Case Class / Class we want to parse.
    * @return List of records parsed into object (type info will be fetched using manifest).
    */
  protected def parseAndExtractRecords[T](resultset: ResultSet, columns: List[RoguDatabaseColumn])(implicit
    m: Manifest[T]): List[T] = {
    if (columns.isEmpty) {
      List[T]()
    } else {
      val col_count = columns.size
      var processed_results: List[T] = List[T]()
      var record_index: BigInt = BigInt.apply(0)
      while (resultset.next()) {
        record_index = record_index.+(1)
        var record_values: Map[String, Any] = Map[String, Any]()
        for (column_index <- 1 to col_count) {
          val column = columns(column_index - 1)
          val record_value = resultset.getString(column.name)
          val serialized_value = parseAndExtractField(record_value, column.`type`)
          record_values += (column.name -> serialized_value)
        }
        val deserializeIntoObject: T = ParseMap[T](record_values)
        processed_results = deserializeIntoObject :: processed_results
      }
      processed_results
    }
  }

  /** Protected method to parse a filed to appropriate type based on the data
    * type of the field's column.
    * @param value value present in the field.
    * @param column_type SQL data type of the field.
    * @return Appropriately parsed field's value
    */
  protected def parseAndExtractField(value: String, column_type: String): Any = {
    val is_time = if (time_data_types.contains(column_type)) true else false
    val is_date = if (date_data_types.contains(column_type)) true else false
    val is_numeric = if (numeric_data_types.contains(column_type)) true else false
    val is_string = if (string_data_types.contains(column_type)) true else false

    if (is_numeric) {
      if (isWholeNumber(value)) {
        BigInt.apply(value)
      } else {
        BigDecimal.apply(value)
      }
    } else if (is_string) {
      value
    } else if (is_date) {
      val pattern: String = "yyyy-MM-dd hh:mm:ss.SSS"
      logger.trace(s"Trimmed Pattern: ${pattern.substring(0, value.length)}")
      val df = new SimpleDateFormat(pattern.substring(0, value.length))
      val date = df.parse(value)
      new Date(date.getTime)
    } else if (is_time) {
      val pattern: String = "yyyy-MM-dd hh:mm:ss.SSS"
      logger.trace(s"Trimmed Pattern: ${pattern.substring(0, value.length)}")
      val df = new SimpleDateFormat(pattern.substring(0, value.length))
      val date = df.parse(value)
      new Timestamp(date.getTime)
    } else {
      throw new IllegalArgumentException(
        s"Trying to parse unsupported data type. Supplied field's data type: $column_type")
    }
  }

  /** Protected method to check if the value supplied
    * is a whole number or is it decimal.
    *
    * Will throw an exception if supplied value is not
    * number (whole or decimal).
    * @param value number to check
    * @return true if supplied value is whole number, false if it's decimal, exception otherwise.
    */
  protected def isWholeNumber(value: String): Boolean = {
    var result: Boolean = true
    value.foreach(x => if (!Character.isDigit(x)) { result = false })
    if (!result) {
      try {
        val parsable = BigDecimal.apply(value)
        logger.trace(s"Parsed value: $parsable")
      } catch {
        case ex: Exception =>
          logger.error("Unable to parse the field as either BigInt or BigDecimal.")
          logger.error(s"Actual Exception Message is: ${ex.getMessage}")
      }
    }
    result
  }
}

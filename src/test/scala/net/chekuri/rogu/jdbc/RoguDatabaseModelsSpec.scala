package net.chekuri.rogu.jdbc

import net.chekuri.rogu.jdbc.RoguDatabaseModels.{
  RoguDatabaseColumn,
  RoguDatabaseRecords,
  RoguDatabaseResult,
  RoguParsedDatabaseResult
}
import net.chekuri.rogu.utilities.RoguLogger
import org.scalatest.flatspec.AnyFlatSpec

class RoguDatabaseModelsSpec extends AnyFlatSpec with RoguLogger {
  val columns: List[RoguDatabaseColumn] =
    List[RoguDatabaseColumn](RoguDatabaseColumn(name = "test", `type` = "test", props = Map[String, String]()))
  val records: RoguDatabaseRecords = RoguDatabaseRecords(values = List[Any](), size = BigInt.apply(1))
  val randomizer = scala.util.Random
  val compile_nanos: Long = randomizer.between(10000, 20000)
  val fetch_nanos: Long = randomizer.between(10000, 20000)
  val process_nanos: Long = randomizer.between(10000, 20000)
  val new_total_positive_nanos: Long = compile_nanos + fetch_nanos + process_nanos + randomizer.between(10000, 20000)
  val new_total_negative_nanos: Long = compile_nanos + fetch_nanos + process_nanos - randomizer.between(10000, 20000)

  logger.info(s"Compile      Nanos: $compile_nanos")
  logger.info(s"Fetch        Nanos: $fetch_nanos")
  logger.info(s"Process      Nanos: $process_nanos")
  logger.info(s"New Positive Nanos: $new_total_positive_nanos")
  logger.info(s"New Negative Nanos: $new_total_negative_nanos")

  "RoguDatabaseResult" should "correctly update TotalNanos when supplied value >= compile_nanos + fetch_nanos + process_nanos" in {
    val payload: RoguDatabaseResult =
      RoguDatabaseResult(columns, records, compile_nanos, fetch_nanos, process_nanos)
    payload.updateTotalNanos(new_total_nanos = BigInt.apply(new_total_positive_nanos))
    assert(new_total_positive_nanos == payload.total_nanos)
  }

  "RoguDatabaseResult" should "correctly NOT update TotalNanos when supplied value < compile_nanos + fetch_nanos + process_nanos" in {
    val payload: RoguDatabaseResult =
      RoguDatabaseResult(columns, records, compile_nanos, fetch_nanos, process_nanos)
    payload.updateTotalNanos(new_total_nanos = BigInt.apply(new_total_negative_nanos))
    assert(new_total_negative_nanos < payload.total_nanos)
  }

  "RoguParsedDatabaseResult" should "correctly update TotalNanos when supplied value >= compile_nanos + fetch_nanos + process_nanos" in {
    val payload: RoguParsedDatabaseResult[Any] =
      RoguParsedDatabaseResult[Any](records.values, compile_nanos, fetch_nanos, process_nanos)
    payload.updateTotalNanos(new_total_nanos = BigInt.apply(new_total_positive_nanos))
    assert(new_total_positive_nanos == payload.total_nanos)
  }

  "RoguParsedDatabaseResult" should "correctly NOT update TotalNanos when supplied value < compile_nanos + fetch_nanos + process_nanos" in {
    val payload: RoguParsedDatabaseResult[Any] =
      RoguParsedDatabaseResult[Any](records.values, compile_nanos, fetch_nanos, process_nanos)
    payload.updateTotalNanos(new_total_nanos = BigInt.apply(new_total_negative_nanos))
    assert(new_total_negative_nanos < payload.total_nanos)
  }
}

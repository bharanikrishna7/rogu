package net.chekuri.rogu.jdbc

import net.chekuri.rogu.utilities.{RoguBenchmark, RoguLogger, RoguThreads}

object RoguDatabaseModels {

  case class RoguDatabaseColumn(name: String, `type`: String, props: Map[String, String])

  case class RoguDatabaseRecords(values: List[Any], size: BigInt)

  case class RoguDatabaseResult(
    columns: List[RoguDatabaseColumn],
    records: RoguDatabaseRecords,
    compile_nanos: BigInt,
    fetch_nanos: BigInt,
    process_nanos: BigInt,
    var total_nanos: BigInt = BigInt.apply(0)
  ) extends RoguThreads with RoguBenchmark with RoguLogger {
    total_nanos = compile_nanos.+(fetch_nanos.+(process_nanos))
    logger.trace(s"initialized new rogu database result record using thread with id: ${getThreadId()}")

    def updateTotalNanos(new_total_nanos: BigInt): Unit = {
      if (new_total_nanos.compareTo(total_nanos) > 0) {
        total_nanos = new_total_nanos
      } else {
        logger.warn("new_total_nanos !< compile_nanos + fetch_nanos + process_nanos")
        logger.warn("not updating total_nanos value.")
      }
    }
  }

  case class RoguParsedDatabaseResult[R](
    result: List[R],
    compile_nanos: BigInt,
    fetch_nanos: BigInt,
    process_nanos: BigInt,
    var total_nanos: BigInt = BigInt.apply(0))
    extends RoguLogger with RoguThreads {
    total_nanos = compile_nanos.+(fetch_nanos.+(process_nanos))
    logger.trace(s"initialized new rogu parsed database result record using thread with id: ${getThreadId()}")

    def updateTotalNanos(new_total_nanos: BigInt): Unit = {
      if (new_total_nanos.compareTo(total_nanos) > 0) {
        total_nanos = new_total_nanos
      } else {
        logger.warn("new_total_nanos !< compile_nanos + fetch_nanos + process_nanos")
        logger.warn("not updating total_nanos value.")
      }
    }
  }
}

package net.chekuri.rogu.utilities

/** Interface which provides benchmarking features.
  */
trait RoguBenchmark extends RoguThreads with RoguLogger {

  /** Benchmark Results.
    *
    * @param result      actual result.
    * @param nanos       time taken to execute task in nanoseconds.
    * @param thread_id   thread id which executed this task.
    * @param thread_name thread name which executed this task.
    * @tparam R Case Class / Class associated with result.
    */
  case class BenchmarkResult[R](
    result: R,
    nanos: BigInt,
    thread_id: Long = getThreadId(),
    thread_name: String = getThreadName()) {
    /** Method to return time taken to execute task in microseconds.
      *
      * @return time taken to execute task in microseconds.
      */
    def nanosToMicro: BigDecimal = BigDecimal.apply(nanos)./(1000)

    /** Method to return time taken to execute task in milliseconds.
      *
      * @return time taken to execute task in milliseconds.
      */
    def nanosToMilli: BigDecimal = nanosToMicro./(1000)

    /** Method to return time taken to execute task in seconds.
      *
      * @return time taken to execute task in seconds.
      */
    def nanosToSecs: BigDecimal = nanosToMilli./(1000)

    /** Method to return time taken to execute task in minutes.
      *
      * @return time taken to execute task in minutes.
      */
    def nanosToMins: BigDecimal = nanosToSecs./(60)
  }

  def Benchmark[R](block: => R): BenchmarkResult[R] = {
    import java.lang.System.nanoTime

    val start_time: Long = nanoTime
    val result = block
    val stop_time: Long = nanoTime
    val execution_time: Long = stop_time - start_time
    BenchmarkResult(result, BigInt.apply(execution_time))
  }
}

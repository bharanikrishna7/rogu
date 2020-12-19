name := "rogu"
version := "0.4"
organization := "net.chekuri"
developers := List(
  Developer(
    "bharanikrishna7",
    "Venkata Bharani Krishna Chekuri",
    "bharanikrishna7@gmail.com",
    url("https://github.com/bharanikrishna7/")
  )
)

lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.13.4",
    /* json4s library dependency */
    libraryDependencies ++= Seq(json4s_native_library),
    /* logging library dependencies */
    libraryDependencies ++= Seq(scala_logging_library, logback_classic_library),
    /* scalatest (test only) + unit test library dependencies */
    libraryDependencies ++= Seq(scalactic_library, scalatest_library),
    /* mysql library dependency for unit tests */
    libraryDependencies ++= Seq(mysql_connector_library),
    /* hikaricp library dependency for unit tests */
    libraryDependencies ++= Seq(hikari_cp_library),
    // set code reformat on compile to true
    scalafmtOnCompile := true
  )

// library versions
val json4s_version: String = "3.7.0-M7"
val mysql_version: String = "8.0.22"
val scala_logging_version: String = "3.9.2"
val scalatest_version: String = "3.2.3"
val logback_version: String = "1.3.0-alpha5"
val hikari_cp_version: String = "3.4.5"

// library modules
val json4s_native_library: ModuleID = "org.json4s" %% "json4s-native" % json4s_version
val scala_logging_library: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % scala_logging_version
val scalactic_library: ModuleID = "org.scalactic" %% "scalactic" % scalatest_version
val scalatest_library: ModuleID = "org.scalatest" %% "scalatest" % scalatest_version % "test"
val logback_classic_library: ModuleID = "ch.qos.logback" % "logback-classic" % logback_version % Test
val mysql_connector_library: ModuleID = "mysql" % "mysql-connector-java" % mysql_version % Test
val hikari_cp_library: ModuleID = "com.zaxxer" % "HikariCP" % hikari_cp_version % Test
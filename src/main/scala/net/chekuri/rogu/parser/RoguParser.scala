package net.chekuri.rogu.parser

import net.chekuri.rogu.utilities.{RoguLogger, RoguThreads}
import org.json4s.DefaultFormats

/** Interface which provides methods
  * to serialize and deserialize between
  * String - Map - Case Class / Class
  */
trait RoguParser extends RoguThreads with RoguLogger {

  /* using implicit formats when available */
  implicit val formats: DefaultFormats.type = DefaultFormats

  /** Method to parse a json string to Case Class / Class.
    *
    * @param json json string to parse
    * @param m    Manifest associated with the class we want to parse to.
    * @tparam T Case Class / Class we want to parse.
    * @return Case Class /Class object with Json Values.
    */
  def ParseJson[T](json: String)(implicit m: Manifest[T]): T = {
    logger.debug(s"Executing Parse operation on thread : ${getThreadName()}")
    import org.json4s.native.JsonMethods._
    parse(json).extract[T]
  }

  /** Method to parse Map to Case Class / Class.
    *
    * @param map hash map or mapping table
    * @param m   Manifest associated with the class we want to parse to.
    * @tparam T Case Class / Class we want to parse.
    * @return Case Class /Class object with Json Values.
    */
  def ParseMap[T](map: Map[String, Any])(implicit m: Manifest[T]): T = {
    val json = MapToJson(map)
    ParseJson(json)
  }

  /** Method to convert a Map to Json String.
    *
    * @param map hash map or mapping table
    * @return Mapping table converted to json string.
    */
  def MapToJson(map: Map[String, Any]): String = {
    import org.json4s.native.Serialization.write
    write(map)
  }

  /** Method to convert a Case Class / Class to Json String.
    *
    * @param payload case class/class we want to convert to json string
    * @param m       Manifest associated with the class we want to parse to.
    * @tparam T Case Class / Class we want to parse.
    * @return Json String value associated with supplied Case Class / Class.
    */
  def ObjectToJson[T](payload: T, pretty: Boolean = false)(implicit m: Manifest[T]): String = {
    logger.debug(s"Executing Serialize operation on thread : ${getThreadName()}")
    if (pretty) {
      import org.json4s.native.Serialization.writePretty
      writePretty(payload)
    } else {
      import org.json4s.native.Serialization.write
      write(payload)
    }
  }
}

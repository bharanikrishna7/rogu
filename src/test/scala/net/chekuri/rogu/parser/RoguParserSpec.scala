package net.chekuri.rogu.parser

import net.chekuri.rogu.helpers.RoguTestModels.{Departments, Employees}
import org.json4s.MappingException
import org.scalatest.flatspec.AnyFlatSpec

class RoguParserSpec extends AnyFlatSpec with RoguParser {
  "ObjectToJson" should "correctly convert object into it's json value" in {
    val dept_no: String = getThreadName()
    val dept_name: String = "ObjectToJson"
    val payload: Departments = Departments(dept_no, dept_name)
    val actual: String = ObjectToJson[Departments](payload)
    val expected: String = s"""{"dept_no":"$dept_no","dept_name":"$dept_name"}"""
    logger.info(s"Actual Value: $actual")
    logger.info(s"Expected Value: $expected")
    assert(actual == expected)
  }

  "MapToJson" should "correctly convert map into it's json value" in {
    val dept_no: String = getThreadName()
    val dept_name: String = "MapToJson"
    val map: Map[String, Any] = Map[String, Any](
      "dept_no" -> dept_no,
      "dept_name" -> dept_name
    )
    val actual: String = MapToJson(map)
    val expected: String = s"""{"dept_no":"$dept_no","dept_name":"$dept_name"}"""
    logger.info(s"Actual Value: $actual")
    logger.info(s"Expected Value: $expected")
    assert(actual == expected)
  }

  "MapToJson" should "corretly convert empty mapping table to json value" in {
    val map: Map[String, Any] = Map[String, Any]()
    val actual: String = MapToJson(map)
    val expected: String = "{}"
    logger.info(s"Actual Value: $actual")
    logger.info(s"Expected Value: $expected")
    assert(actual == expected)
  }

  "ParseJson" should "correctly deserialize json string into instance of object of supplied object type when correct type is supplied" in {
    val dept_no: String = getThreadName()
    val dept_name: String = "ParseJson"
    val original: Departments = Departments(dept_no, dept_name)
    val json: String = ObjectToJson[Departments](original)
    logger.info(s"Generated Json: $json")
    val restored: Departments = ParseJson[Departments](json)
    assert(original.dept_no == dept_no)
    assert(restored.dept_no == dept_no)
    assert(original.dept_name == dept_name)
    assert(restored.dept_name == dept_name)
  }

  "ParseJson" should "throw exception when supplied object type does not correspond to values in the associated json string" in {
    val dept_no: String = getThreadName()
    val dept_name: String = "ParseJson"
    val original: Departments = Departments(dept_no, dept_name)
    val json: String = ObjectToJson[Departments](original)
    logger.info(s"Generated Json: $json")
    assertThrows[MappingException](ParseJson[Employees](json))
  }

  "ParseMap" should "correctly deserialize map into instance of object of supplied object type when correct type is supplied" in {
    val dept_no: String = getThreadName()
    val dept_name: String = "ParseMap"
    val original: Map[String, Any] = Map[String, Any](
      "dept_no" -> dept_no,
      "dept_name" -> dept_name
    )
    val restored: Departments = ParseMap[Departments](original)
    assert(restored.dept_no == dept_no)
    assert(restored.dept_name == dept_name)
  }

  "ParseMap" should "throw exception when supplied object type does not correspond to values in the associated map" in {
    val dept_no: String = getThreadName()
    val dept_name: String = "ParseMap"
    val original: Map[String, Any] = Map[String, Any](
      "dept_no" -> dept_no,
      "dept_name" -> dept_name
    )
    assertThrows[MappingException](ParseMap[Employees](original))
  }
}

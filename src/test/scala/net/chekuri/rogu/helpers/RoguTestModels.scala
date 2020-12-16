package net.chekuri.rogu.helpers

import java.sql.Date

object RoguTestModels {

  case class Employees(
    emp_no: Long,
    birth_date: Date,
    first_name: String,
    last_name: String,
    gender: Char,
    hire_date: Date)

  case class Titles(emp_no: Long, title: String, from_date: Date, to_date: Option[Date])

  case class Salaries(emp_no: Long, salary: Long, from_date: Date, to_date: Date)

  case class Departments(dept_no: String, dept_name: String)

}

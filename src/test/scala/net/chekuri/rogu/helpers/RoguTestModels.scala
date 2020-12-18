package net.chekuri.rogu.helpers

import java.sql.{Date, Timestamp}

object RoguTestModels {

  case class Employees(
    emp_no: Long,
    birth_date: Date,
    first_name: String,
    last_name: String,
    gender: Char,
    hire_date: Date)

  case class Departments(dept_no: String, dept_name: String)

  case class Author(
    author_id: Long,
    name: String,
    last_name: Option[String],
    initials: Option[String],
    orcid: Option[String],
    synonyms: Option[String])

  case class Version(
    rfam_release: Double,
    rfam_release_date: Date,
    number_families: Int,
    embl_release: String
  )

  case class Clan(
    clan_acc: String,
    id: Option[String],
    previous_id: Option[String],
    description: Option[String],
    author: Option[String],
    comment: Option[String],
    created: Timestamp,
    updated: Timestamp)

  case class FamilyAuthorBoolean(
    rfam_acc: String,
    author_id: Long,
    desc_order: Boolean
  )

  case class FamilyAuthor(
    rfam_acc: String,
    author_id: Long,
    desc_order: Int
  )
}

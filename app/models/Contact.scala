package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.data.validation._
import play.api.Play.current

case class Contact(id: Pk[Long], name: String, phone: String)

object Contact {
  def all(filter: String = "%"): List[Contact] = DB.withConnection { implicit c =>
    SQL(
      """ select * from contact
          where contact.name like {filter}
      """).on('filter -> filter).as(contact *)
  }

  def create(name: String, phone: String) = {
    DB.withConnection { implicit c =>
      SQL("insert into contact (name, phone) values ({name}, {phone})").on(
        'name -> name,
        'phone -> phone
      ).executeUpdate()
    }
  }

  def delete(id: Long) = {
    DB.withConnection { implicit c =>
      SQL("delete from contact where id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

  /*
  * Regexp for phone
  * */
  val phoneFormat = """^((8|\+7)[\- ]?)?(\(?\d{3}\)?[\- ]?)?[\d\- ]{7,10}$""".r

  val phoneIsCorrect = Constraints.pattern(
    phoneFormat,
    "constraint.phone",
    "error.phone")

  def findByPhone(phone: String): Option[Contact] = {
    DB.withConnection { implicit connection =>
      SQL("select * from contact where phone = {phone}").on('phone -> phone).as(contact.singleOpt)
    }
  }

  def phoneIsFree(phone: String): Boolean = findByPhone(phone).isEmpty

  /*
  * Check: is phone unique? If not raise a caution
  * */
  val phoneIsUnique: Constraint[String] = Constraint("Unique")({
    plainText =>
      val errors = plainText match {
        case phone if !phoneIsFree(phone) => Seq(ValidationError("phone already exists"))
        case _ => Nil
      }
      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })

  /*
  * Contact format
  * */
  val contact = {
    get[Pk[Long]]("id") ~
      get[String]("name") ~
        get[String]("phone") map {
      case id~name~phone => Contact(id, name, phone)
    }
  }

}

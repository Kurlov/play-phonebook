package controllers

import anorm.{Pk, NotAssigned}
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Contact

object Application extends Controller {

  def index = Action {
    Redirect(routes.Application.contacts())
  }

  /*
  * Show all contacts. Filter helps us to select certain contacts. By default filter equals to any string
  * */
  def contacts(filter: String = "%") = Action {
    Ok(views.html.index(Contact.all("%"+filter+"%")))
  }

  /*
  Handle adding a new contact. If error raise 400
  * */
  def addContact = Action { implicit request =>
      contactForm.bindFromRequest.fold(
      errors => BadRequest(views.html.addContact(errors)),
      data => {
        Contact.create(data.name, data.phone)
        Redirect(routes.Application.contacts())
      }
    )
  }

  def showContactForm = Action {
    Ok(views.html.addContact(contactForm))
  }


  def deleteContact(id: Long) = Action {
    Contact.delete(id)
    Redirect(routes.Application.contacts())
  }


  /*
  * Contact Form
  * */
  val contactForm = Form(
    mapping(
    "id" -> ignored(NotAssigned:Pk[Long]),
    "name" -> nonEmptyText,
    "phone" -> nonEmptyText.verifying(Contact.phoneIsCorrect, Contact.phoneIsUnique)
    )(Contact.apply)(Contact.unapply)
  )

}
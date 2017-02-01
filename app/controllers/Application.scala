package controllers

import javax.inject._

import play.api.mvc._
import dao.TodoDAO
import models.Todo
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsError, JsValue, Json}

@Singleton
class Application @Inject()(todoDAO: TodoDAO) extends Controller {

    implicit val todoWrites = Json.writes[Todo]
    implicit val todoReads = Json.reads[Todo]

    def index = readAll

    def create = Action.async(BodyParsers.parse.json) {
        implicit request =>
            val newTodo = request.body.validate[Todo]
            newTodo.fold(
                errors => {
                    scala.concurrent.Future(
                        BadRequest(Json.obj("status" -> "400", "message" -> JsError.toJson(errors)))
                    )
                },
                newTodo => {
                    for {
                        success: Int <- todoDAO.insert(newTodo)
                    } yield success match {
                        case 1 => Ok(Json.obj("status" -> "200", "message" -> "The new todo has been saved"))
                        case 0 => Ok(Json.obj("status" -> "400", "message" -> "The new todo couldn't be saved"))
                    }
                }
            )
    }

    def read(id: Long) = Action.async {
        implicit request =>
            val todos = todoDAO.findById(id)
            todos.map(todo => todo match {
                case todo: Todo => Ok(Json.obj("status" -> "200", "data" -> Json.toJson(todo)))
                case _          => Ok(Json.obj("status" -> "400", "message" -> "The need you were looking for couldn't be found"))
            })
    }

    def readAll = Action.async {
        implicit request =>
            val todos = todoDAO.all()
            todos.map(todo => todo match {
                case todo: Todo => Ok(Json.obj("status" -> "200", "data" -> Json.toJson(todo)))
                case _          => Ok(Json.obj("status" -> "400", "message" -> "The need you were looking for couldn't be found"))
            })
    }

    def update(id: Long) = Action.async(BodyParsers.parse.json) {
        implicit request =>
            val updatedTodo = request.body.validate[Todo]
            updatedTodo.fold(
                errors => {
                    scala.concurrent.Future(
                        BadRequest(Json.obj("status" -> "400", "message" -> JsError.toJson(errors)))
                    )
                },
                updatedTodo => {
                    for {
                        success: Int <- todoDAO.update(id, updatedTodo)
                    } yield success match {
                        case 1 => Ok(Json.obj("status" -> "200", "message" -> "Todo has been updated"))
                        case 0 => Ok(Json.obj("status" -> "400", "message" -> "Todo couldn't be updated"))
                    }
                }
            )
    }

    def delete(id: Long) = Action.async {
        implicit request =>
            todoDAO.delete(id).map(i => i match {
                case 1 => Ok(Json.obj("status" -> "200", "message" -> "Todo has been deleted"))
                case _ => Ok(Json.obj("status" -> "400", "message" -> "Todo couldn't be deleted"))
            })
    }
}
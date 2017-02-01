package dao

import javax.inject.Inject

import models.Todo
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.profile

import scala.concurrent.Future

/**
  * Created by DU on 31/01/2017.
  */

class TodoDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

    import profile.api._

    private class TodosTable(tag: Tag) extends Table[Todo](tag, "todo") {

        def idTodo = column[Long]("idTodo", O.PrimaryKey, O.AutoInc)

        def text = column[String]("text")

        def * = (idTodo, text) <> (Todo.tupled, Todo.unapply _)
    }

    private val todos = TableQuery[TodosTable]

    def findById(idTodo: Long): Future[Option[Todo]] = db.run(todos.filter(_.idTodo === idTodo).result.headOption)

    def all(): Future[Seq[Todo]] = db.run(todos.result)

    def insert(todo: Todo): Future[Int] = db.run(todos += todo)

    def update(id: Long, todo: Todo): Future[Int] = {
        val todoToUpdate : Todo = todo.copy(id)
        db.run(todos.filter(_.idTodo === id).update(todoToUpdate))
    }

    def delete(idTodo: Long): Future[Int] = db.run(todos.filter(_.idTodo === idTodo).delete)
}

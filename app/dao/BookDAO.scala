package dao

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import slick.driver.JdbcProfile
import slick.backend.DatabaseConfig
import slick.ast.BaseTypedType
import io.strongtyped.active.slick._
import io.strongtyped.active.slick.Lens._

import models.{Book, BookId}

trait ProfileProvider extends JdbcProfileProvider {
  val dc = DatabaseConfig.forConfig[JdbcProfile]("mydb")
  type JP = JdbcProfile
  val jdbcProfile = dc.driver
  val db = dc.db
}

class BookDAO extends EntityActions with ProfileProvider {
  import jdbcProfile.api._

  val baseTypedType = implicitly[BaseTypedType[Id]]

  type Entity = Book
  type Id = BookId
  type EntityTable = BookTable

  val tableQuery = TableQuery[BookTable]

  def $id(table: BookTable): Rep[Id] = table.id

  val idLens = lens { model: Book => model.id  }
  { (model, id) => model.copy(id = id) }

  class BookTable(tag: Tag) extends Table[Book](tag, "books") {
    def id = column[Id]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (name, Rep.Some(id)) <> ((Book.apply _).tupled, Book.unapply)
  }

  def findByName(name:String): DBIO[Seq[Book]] = {
    tableQuery.filter(_.name === name).result
  }

  def run[R](dbioAction: DBIOAction[R, NoStream, Nothing]) = db.run(dbioAction)
}

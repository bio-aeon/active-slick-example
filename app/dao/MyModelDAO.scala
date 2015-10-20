package dao

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import slick.driver.JdbcProfile
import slick.backend.DatabaseConfig
import slick.ast.BaseTypedType
import io.strongtyped.active.slick._
import io.strongtyped.active.slick.Lens._

import models.MyModel

class MyModelDAO extends EntityActions with JdbcProfileProvider {
  val dc = DatabaseConfig.forConfig[JdbcProfile]("mydb")
  type JP = JdbcProfile
  val jdbcProfile = dc.driver
  val db = dc.db

  import jdbcProfile.api._
  val baseTypedType = implicitly[BaseTypedType[Id]]

  type Entity = MyModel
  type Id = Int
  type EntityTable = MyModelTable

  val tableQuery = TableQuery[MyModelTable]

  def $id(table: MyModelTable): Rep[Int] = table.id

  val idLens = lens { model: MyModel => model.id  }
  { (model, id) => model.copy(id = id) }

  class MyModelTable(tag: Tag) extends Table[MyModel](tag, "models") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def * = (name, Rep.Some(id)) <> ((MyModel.apply _).tupled, MyModel.unapply)
  }

  def findByName(name:String): DBIO[Seq[MyModel]] = {
    tableQuery.filter(_.name === name).result
  }

  def run[R](dbioAction: DBIOAction[R, NoStream, Nothing]) = db.run(dbioAction)
}

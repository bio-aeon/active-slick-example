package models

import slick.lifted.MappedTo

case class Book(name: String, id: Option[BookId] = None)

case class BookId(value: Int) extends AnyVal with MappedTo[Int]

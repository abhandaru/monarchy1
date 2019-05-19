package monarchy.dal

import scala.concurrent.{ExecutionContext, Future}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait QueryClient {
  import PostgresProfile.api._
  def all[E](query: Query[Table[E], E, Seq]): Future[Seq[E]]
  def first[E](query: Query[Table[E], E, Seq]): Future[Option[E]]
  def write[E](dbio: DBIO[E]): Future[E]
}

case class QueryClientImpl(
  cfg: DatabaseConfig[JdbcProfile]
)(implicit ec: ExecutionContext) extends QueryClient {
  import PostgresProfile.api._

  private val connection = cfg.db

  // Methods for reading.
  override def all[E](query: Query[Table[E], E, Seq]): Future[Seq[E]] = {
    connection.run(query.result)
  }

  override def first[E](query: Query[Table[E], E, Seq]): Future[Option[E]] = {
    all(query.take(1)).map(_.headOption)
  }

  // Methods for writing.
  override def write[E](dbio: DBIO[E]): Future[E] = {
    connection.run(dbio.transactionally)
  }
}

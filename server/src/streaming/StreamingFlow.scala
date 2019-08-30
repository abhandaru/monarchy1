package monarchy.streaming

import akka.actor._
import akka.http.scaladsl.model.ws.{BinaryMessage, TextMessage, Message}
import akka.stream.actor.ActorPublisher
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Source, Flow, Sink}
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

object StreamingFlow {
  def apply(actorPub: ActorRef)(
    implicit
    ec: ExecutionContext,
    materializer: ActorMaterializer
  ): Flow[Message, Message, _] = {
    Flow[Message]
      .mapConcat(drainNonText)
      .mapAsync(1)(resolveText)
      .merge(Source.fromPublisher(ActorPublisher(actorPub)))
      .map(liftAsMessage)
  }

  def drainNonText(m: Message)(implicit mat: ActorMaterializer): List[TextMessage] = m match {
    case tm: TextMessage => List(tm)
    case bm: BinaryMessage =>
      // ignore binary messages but drain content to avoid the stream being clogged
      bm.dataStream.runWith(Sink.ignore)
      Nil
  }

  def resolveText(tm: TextMessage)(implicit ec: ExecutionContext, mat: ActorMaterializer): Future[String] = {
    tm.toStrict(5.seconds).map(_.text)
  }

  def liftAsMessage(text: String): Message = {
    TextMessage(Source.single(text))
  }
}

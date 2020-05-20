package org.carbonateresearch.conus.dispatchers

import akka.actor.Actor

abstract class ParrallelModellerCollectorActor extends Actor {
  /*
  var resultsList = scala.collection.mutable.ListBuffer.empty[OldSingleModelWithResults]

  override def receive = {

    case modelsList: List[OldChainableCalculation] => {
      val initialCount = modelsList.size
      println("Now dispatching a modelsList of size " + modelsList.size.toString)

      implicit val timeout = Timeout(5.seconds)

      val future:List[Future[Any]] = modelsList.map(m =>
        context.actorOf(Props(new ParrallelSingleModelRunnerActor)) ? m )

      implicit val ec = global

      future.foreach(f =>
      f onComplete {
        case Success(model) => {
          model match {
            case m:OldSingleModelWithResults => {
          resultsList += m
          println(resultsList)}
            }}
      })

      sender ! future

      println("Dispatcher should have the results")

    }
    case s:String       => {
      println(s)
      sender ! List("This is a response from the Collector", "Did you receive it?")
    }
    case _       => println("Sample type not handled by Modeler")
  }
  */
}

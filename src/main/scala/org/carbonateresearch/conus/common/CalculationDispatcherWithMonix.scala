package org.carbonateresearch.conus.common
import scala.concurrent.Future
import monix.execution.Scheduler
import monix.execution.Scheduler.Implicits.global
import monix.execution.CancelableFuture
import monix.eval.Task

import scala.util.{Failure, Success}

class CalculationDispatcherWithMonix extends CalculationDispatcher {
  override def calculateModelsList(models: List[ChainableCalculation]): Future[List[RunnedModel]] = {

    val initialCount = models.size

    println("Initiating a run on " + initialCount.toString + " models. Based on Monix")

    val t0 = System.nanoTime()
    val tasks = models.map(c => Task(c.evaluate(t0)).runToFuture)

    //val results = Task.gather(tasks).map(_.toList)

    Future(List())
  }
}

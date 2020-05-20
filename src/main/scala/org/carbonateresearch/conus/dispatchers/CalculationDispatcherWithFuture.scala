package org.carbonateresearch.conus.dispatchers

import org.carbonateresearch.conus.common.{SingleModel, SingleModelResults}

import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future


class CalculationDispatcherWithFuture extends CalculationDispatcher {
  override val typeOfDispatcher: String = java.lang.Runtime.getRuntime.availableProcessors.toString
  override def calculateModelsList(models: List[SingleModel]): Future[List[SingleModelResults]] = {
    implicit val ec = global
    println(outputString(models))
    val t0 = System.nanoTime()
    val newResults: Future[List[SingleModelResults]] = Future.sequence(models.map(c => Future(c.evaluate(t0))))
    newResults
  }

  }


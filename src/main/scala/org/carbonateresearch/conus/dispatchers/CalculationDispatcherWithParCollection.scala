package org.carbonateresearch.conus.dispatchers

import org.carbonateresearch.conus.common.{SingleModel, SingleModelResults}

import scala.collection.parallel.CollectionConverters._
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future



class CalculationDispatcherWithParCollection extends CalculationDispatcher {
  override val typeOfDispatcher: String = java.lang.Runtime.getRuntime.availableProcessors.toString
  override def calculateModelsList(models: List[SingleModel]): Future[List[SingleModelResults]] = {
    val initialCount = models.size
    implicit val ec = global
    Console.println(outputString(models))

    val t0 = System.nanoTime()
    val newResults:List[SingleModelResults] = models.par.map(x => x.evaluate(t0)).toList

    Future(newResults)
  }
  }


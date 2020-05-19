package org.carbonateresearch.conus.common

import scala.collection.parallel.CollectionConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.global



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


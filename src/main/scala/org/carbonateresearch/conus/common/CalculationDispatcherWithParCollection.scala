package org.carbonateresearch.conus.common

import scala.collection.parallel.CollectionConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.global



class CalculationDispatcherWithParCollection extends CalculationDispatcher {

  override def calculateModelsList(models: List[ChainableCalculation]): Future[List[RunnedModel]] = {
    val initialCount = models.size
    implicit val ec = global
    Console.println(outputString(models))

    val t0 = System.nanoTime()
    val newResults:List[RunnedModel] = models.par.map(x => x.evaluate(t0)).toList

    Future(newResults)
  }
  }


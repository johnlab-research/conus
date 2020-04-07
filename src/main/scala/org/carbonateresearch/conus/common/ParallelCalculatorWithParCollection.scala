package org.carbonateresearch.conus.common
import org.carbonateresearch.conus.oldies.{OldChainableCalculation, OldSingleModelWithResults}

import scala.collection.parallel.CollectionConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.global



class ParallelCalculatorWithParCollection extends ParallelCalculator {
  override def calculateModelsList(models: List[OldChainableCalculation]): Future[List[OldSingleModelWithResults]] = {
    val initialCount = models.size
    implicit val ec = global

    println("Initiating a run on " + models.size.toString + " models.")

    val t0 = System.nanoTime()
    val newResults = models.par.map(x => x.evaluate(t0))

    Future(List())
  }
  }


package org.carbonateresearch.conus.common
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future
import Console.{GREEN, RED, RESET, UNDERLINED, YELLOW_B}
import java.lang.System.lineSeparator

import org.carbonateresearch.conus.oldies.{OldChainableCalculation, OldSingleModelWithResults}

class ParallelCalculatorWithFuture extends ParallelCalculator {
  override def calculateModelsList(models: List[OldChainableCalculation]): Future[List[OldSingleModelWithResults]] = {
    implicit val ec = global

    Console.println(outputString(models))
    val t0 = System.nanoTime()
    val newResults: Future[List[OldSingleModelWithResults]] = Future.sequence(models.map(c => Future(c.evaluate(t0))))
    newResults
  }

  }


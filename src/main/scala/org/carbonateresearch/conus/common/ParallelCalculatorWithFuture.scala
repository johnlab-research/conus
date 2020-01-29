package org.carbonateresearch.conus.common
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future
import Console.{GREEN, RED, RESET, YELLOW_B, UNDERLINED}
import java.lang.System.lineSeparator

class ParallelCalculatorWithFuture extends ParallelCalculator {
  override def calculateModelsList(models: List[ChainableCalculation]): Future[List[SingleModelWithResults]] = {
    implicit val ec = global

    Console.println(outputString(models))
    val t0 = System.nanoTime()
    val newResults: Future[List[SingleModelWithResults]] = Future.sequence(models.map(c => Future(c.evaluate(t0))))
    newResults
  }

  }


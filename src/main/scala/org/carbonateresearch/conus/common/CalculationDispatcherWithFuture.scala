package org.carbonateresearch.conus.common
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future
import Console.{GREEN, RED, RESET, UNDERLINED, YELLOW_B}
import java.lang.System.lineSeparator


class CalculationDispatcherWithFuture extends CalculationDispatcher {
  override def calculateModelsList(models: List[ChainableCalculation]): Future[List[RunnedModel]] = {
    implicit val ec = global

    Console.println(outputString(models))
    val t0 = System.nanoTime()
    val newResults: Future[List[RunnedModel]] = Future.sequence(models.map(c => Future(c.evaluate(t0))))
    newResults
  }

  }


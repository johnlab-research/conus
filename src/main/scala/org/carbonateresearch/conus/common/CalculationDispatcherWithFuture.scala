package org.carbonateresearch.conus.common
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future
import java.lang.Runtime._
import Console.{GREEN, RED, RESET, UNDERLINED, YELLOW_B}
import java.lang.System.lineSeparator


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


package org.carbonateresearch.conus.dispatchers

import org.carbonateresearch.conus.common.{SingleModel, SingleModelResults}

import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future

class CalculationDispatcherSequential extends CalculationDispatcher{
  override val typeOfDispatcher: String = 1.toString
override def calculateModelsList(models: List[SingleModel]): Future[List[SingleModelResults]] = {
  implicit val ec = global

  val t0 = System.nanoTime()
  val results:List[SingleModelResults] = models.map(c => c.evaluate(t0))

  Future(results)
}
}

package org.carbonateresearch.conus.common
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future
import scala.util.Success

class CalculationDispatcherSequential extends CalculationDispatcher{
  override val typeOfDispatcher: String = 1.toString
override def calculateModelsList(models: List[ChainableCalculation]): Future[List[EvaluatedModel]] = {
  implicit val ec = global

  val t0 = System.nanoTime()
  val results:List[EvaluatedModel] = models.map(c => c.evaluate(t0))

  Future(results)
}
}

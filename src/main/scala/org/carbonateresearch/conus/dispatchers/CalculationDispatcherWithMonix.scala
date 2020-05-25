/*
 * Copyright © 2020 by Cédric John.
 *
 * This file is part of CoNuS.
 *
 * CoNuS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * CoNuS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CoNuS. If not, see <http://www.gnu.org/licenses/>.
 */

package org.carbonateresearch.conus.dispatchers

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.carbonateresearch.conus.common.{SingleModel, SingleModelResults}
import scala.concurrent.Future

class CalculationDispatcherWithMonix extends CalculationDispatcher {
  override val typeOfDispatcher: String = java.lang.Runtime.getRuntime.availableProcessors.toString
  override def calculateModelsList(models: List[SingleModel]): Future[List[SingleModelResults]] = {

    val initialCount = models.size

    println("Initiating a run on " + initialCount.toString + " models. Based on Monix")

    val t0 = System.nanoTime()
    val tasks = models.map(c => Task(c.evaluate(t0)).runToFuture)

    //val results = Task.gather(tasks).map(_.toList)

    Future(List())
  }
}

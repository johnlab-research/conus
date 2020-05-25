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

import org.carbonateresearch.conus.common.{SingleModel, SingleModelResults}

import scala.collection.parallel.CollectionConverters._
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future



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


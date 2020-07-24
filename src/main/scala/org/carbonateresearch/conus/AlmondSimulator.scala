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

package org.carbonateresearch.conus

import org.carbonateresearch.conus.common.ModelCalculationSpace
import org.carbonateresearch.conus.dispatchers.{AlmondLoggerType, EventLogger}
import org.carbonateresearch.conus.notebook.AlmondDisplay
import org.carbonateresearch.conus.simulators.AkkaCentralSimulatorActor
import org.carbonateresearch.conus.simulators.Simulator


class AlmondSimulator(implicit kernel: almond.api.JupyterApi) extends Simulator {
  private var cellID:String = "none"
  val display = new AlmondDisplay
  display.registerDisplays
  AkkaCentralSimulatorActor.simulatorInterface = this


  def evaluate(models: ModelCalculationSpace)(implicit kernel: almond.api.JupyterApi): Unit = {
    cellID = java.util.UUID.randomUUID().toString
    kernel.publish.html("dude", cellID)
    AkkaCentralSimulatorActor.evaluate(models,AlmondLoggerType(kernel,cellID))
  }

}

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
import org.carbonateresearch.conus.dispatchers.ConsoleLoggerType
import org.carbonateresearch.conus.simulators.{AkkaCentralSimulatorActor, Simulator}


class BasicSimulator extends Simulator {
  AkkaCentralSimulatorActor.simulatorInterface = this
  def evaluate(models: ModelCalculationSpace): Unit = {
    AkkaCentralSimulatorActor.evaluate(models,ConsoleLoggerType())
  }

}

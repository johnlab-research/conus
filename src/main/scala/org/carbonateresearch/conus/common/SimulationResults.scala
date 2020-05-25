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

package org.carbonateresearch.conus.common

import org.carbonateresearch.conus.grids.GridElement

trait SimulationResults {
  def getStepResult[T](stepNumber:Int, k:ModelVariable[T]): GridElement
  def prettyPrint[T](k:ModelVariable[T],step:Int,coordinates:Seq[Int]):String
  def isDefinedAt(step:Int,coordinates:Seq[Int]):Boolean
  def variableIsDefinedAt[T](k:ModelVariable[T],step:Int,coordinates:Seq[Int]):Boolean
}



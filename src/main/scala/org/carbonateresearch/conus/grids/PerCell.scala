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

package org.carbonateresearch.conus.grids

import org.carbonateresearch.conus.common.{CalculationParametersIOLabels, Combinatorial, InitialCondition}

case class PerCell(variableName:CalculationParametersIOLabels, value:List[(Any,Seq[Int])]) extends GridValueDescriptor with Combinatorial {
  def setOfValues:List[List[(Any,Seq[Int])]] = {


      val valueAsLists:List[(List[Any],Seq[Int])] = {
        value.map(v => {v._1 match {
          case l:List[Any] => (l,v._2)
          case l:Any => (List(l),v._2)}
        })}

    val nestedList:List[List[Any]] = combineListOfLists(valueAsLists.map(x => x._1))
    val allPossibleValues: List[List[(Any,Seq[Int])]] = nestedList.map(nl => nl.zip(valueAsLists.unzip._2))

      allPossibleValues
    }
  }

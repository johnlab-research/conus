package org.carbonateresearch.conus.common
import org.carbonateresearch.conus.grids.GridValueDescriptor

case class InitialConditions(valueCoordinates:GridValueDescriptor*) extends Combinatorial {

  val conditions: List[InitialCondition] = {
    valueCoordinates.flatMap(v => v.setOfValues.map(s => InitialCondition(v.variableName,s))).toList
    }
  }


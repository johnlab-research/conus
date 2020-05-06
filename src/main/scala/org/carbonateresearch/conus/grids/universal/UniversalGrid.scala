package org.carbonateresearch.conus.grids.universal
import org.carbonateresearch.conus.common.CalculationParametersIOLabels
import org.carbonateresearch.conus.grids.{Grid, GridElement}
import java.lang.System.lineSeparator
import breeze.linalg._

case class UniversalGrid(override val gridGeometry:Seq[Int],
                         override val nbSteps:Int,
                         override val variableMap:Map[CalculationParametersIOLabels,Int],
                        private val underlyingGrid:DenseVector[GridElement]) extends Grid {

  def getVariableAtCellForTimeStep[T](key:CalculationParametersIOLabels, coordinates:Seq[Int])(implicit timeStep: TimeStep):T = {
    val index:Int = variableMap(key)
    underlyingGrid(timeStep).getValueAtCell(index,coordinates)
  }

  def getVariableForTimeStep(key:CalculationParametersIOLabels)(implicit timeStep: TimeStep):GridElement = {
    reduceToOneVariable(variableMap(key)).getTimeStep(timeStep)
  }

  def getTimeStep(timeStep:TimeStep):GridElement = {
    underlyingGrid(timeStep)
  }

  def setAtCell[T](key:CalculationParametersIOLabels,value:T, coordinates:Dimensions)(implicit timeStep: TimeStep):Unit = {
    underlyingGrid(timeStep).setAtCell(variableMap(key),value,coordinates)
  }

  def setManyAtCell(keys:Seq[CalculationParametersIOLabels],values:Seq[Any], coordinates:Dimensions)(implicit timeStep: TimeStep):Unit = {
    keys.foreach(k => setAtCell(k,values(keys.indexOf(k)),coordinates))
  }

  def setManyForTimeStep(keys:Seq[CalculationParametersIOLabels],values:Seq[Any])(implicit timeStep: TimeStep):Unit = {
    val keysIndex:Seq[Int] = keys.map(k => variableMap(k))
    keys.foreach(k => underlyingGrid(timeStep).setManyForAllCells(keysIndex,values))
  }

  def initializeGrid(keys:Seq[CalculationParametersIOLabels],values:Seq[Any]):Unit = {
    (0 until nbSteps).foreach(t => setManyForTimeStep(keys,values)(t))
  }

  private def reduceToOneVariable(key:Int):UniversalGrid = {
    val updatedGrid = this.copy(gridGeometry, nbSteps, variableMap,underlyingGrid)
    (0 until nbSteps).foreach(i => updatedGrid.underlyingGrid(i).getValueForAllCells(key))
    updatedGrid
  }

  override def toString:String = {
    val cString = gridGeometry.size match {
      case 1 => ",X-Coordinate"
      case 2 => ",X-Coordinate,Y-Coordinate"
      case _ => ",X-Coordinate,Y-Coordinate,Z-Coordinate"
    }
    "Step"+cString+variableMap.map(x => ","+x._1.toString).foldLeft("")(_+_)+lineSeparator()+
    (0 until nbSteps).map(s => {
      underlyingGrid(s).toString()
    }).foldLeft("")(_+_)
  }
  def toString(timeStep:TimeStep):String = underlyingGrid(timeStep).toString()
  def toString(timeStep:TimeStep,keys:CalculationParametersIOLabels*):String = underlyingGrid(timeStep).toString()

}

object UniversalGrid {
  def apply(gridGeometry:Seq[Int], nbSteps:Int, variableMap:Map[CalculationParametersIOLabels,Int]):UniversalGrid = {
     val underlyingGrid = DenseVector.tabulate[GridElement](nbSteps){i => UniversalGrid1D(gridGeometry,variableMap.size,Seq(i))}
    new UniversalGrid(gridGeometry, nbSteps, variableMap, underlyingGrid)
  }
}
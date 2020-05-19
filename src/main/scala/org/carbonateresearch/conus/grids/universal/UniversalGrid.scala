package org.carbonateresearch.conus.grids.universal
import org.carbonateresearch.conus.common.CalculationParametersIOLabels
import org.carbonateresearch.conus.grids.{Grid, GridElement, GridStringFormatter}
import java.lang.System.lineSeparator

import scala.collection.immutable.ListMap
import breeze.linalg._

case class UniversalGrid(override val gridGeometry:Seq[Int],
                         override val nbSteps:Int,
                         override val variableMap:Map[CalculationParametersIOLabels,Int],
                        private val underlyingGrid:DenseVector[GridElement]) extends Grid {

  private val EOL = lineSeparator()

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
    coordinates.size match {
      case 0 => initializeGrid(Seq(key),Seq(value))
      case _ => underlyingGrid (timeStep).setAtCell (variableMap (key), value, coordinates)
    }
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
      case 1 => ",X"
      case 2 => ",X,Y"
      case _ => ",X,Y,Z"
    }
    "Timestep"+cString+ListMap(variableMap.toSeq.sortBy(_._2):_*).map(x => ","+x._1.toString).foldLeft("")(_+_)+lineSeparator()+
    (0 until nbSteps).map(s => {
      underlyingGrid(s).toString()
    }).foldLeft("")(_+_)
  }

  def printGridResults:String = {
    val formatter = new GridStringFormatter
    formatter.printAsGrid(this.toString)
  }

  def summary:String = printGridResults
}

object UniversalGrid {
  def apply(gridGeometry:Seq[Int], nbSteps:Int, variableMap:Map[CalculationParametersIOLabels,Int]):UniversalGrid = {
     val underlyingGrid = DenseVector.tabulate[GridElement](nbSteps){i => UniversalGrid1D(gridGeometry,variableMap.size,Seq(i))}

    new UniversalGrid(gridGeometry, nbSteps, variableMap, underlyingGrid)
  }
}
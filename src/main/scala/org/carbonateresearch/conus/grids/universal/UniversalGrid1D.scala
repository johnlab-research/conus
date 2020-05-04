package org.carbonateresearch.conus.grids.universal
import org.carbonateresearch.conus.common.CalculationParametersIOLabels
import org.carbonateresearch.conus.grids.{Grid, GridElement}
import breeze.linalg._
import breeze.numerics._

case class UniversalGrid1D(gridGeometry:Seq[Int],
                           private val underlyingGrid:DenseVector[DataVector]) extends GridElement {
  override val vecSize:Int = gridGeometry.head
  override val nbOfVariables:Int = gridGeometry.tail.head
  override def toString(timeStep:Int):String = {"TODO"}
  override def toString(timeStep:Int,keys:CalculationParametersIOLabels*):String = {"TODO"}

  override def setAtCell[T](key:Int,value:T, coordinates:Seq[Int]):Unit = {
    underlyingGrid(coordinates.head).set(key,value)
  }

  override def setManyAtCell[T](keys:Seq[Int],values:Seq[T],coordinates:Seq[Int]):Unit = {
    keys.foreach(k => this.setAtCell(k,values(keys.indexOf((k))),coordinates))
  }

  override def setForAllCells[T](key:Int,value:T):Unit = {
    gridGeometry.foreach(c => underlyingGrid(c).set(key,value))
  }

  override def setManyForAllCells[T](keys:Seq[Int],values:Seq[T]):Unit = {
    keys.foreach(k => this.setForAllCells(k,values(keys.indexOf(k))))
  }

  override def getValueAtCell[T](key:Int,coordinates:Seq[Int]):T = {
    underlyingGrid(coordinates.head).get(key)
  }

  override def getValueForAllCells[T](key:Int):UniversalGrid1D = {
    reduceToOneVariable(key)
  }

  private def reduceToOneVariable(key:Int):UniversalGrid1D = {
    val updatedVec = new DenseVector[DataVector](vecSize).map(i => {DataVector(1)})
    (0 until vecSize).foreach(i => updatedVec(i).set(0,this.getValueAtCell(key,Seq(i))))
    new UniversalGrid1D(gridGeometry,updatedVec)
  }
}

object UniversalGrid1D {
  def apply(gridGeometry:Seq[Int]):UniversalGrid1D = {
    val vecSize = gridGeometry.head
    val nbOfVariables = gridGeometry.tail.head
    val underlyingGrid = new DenseVector[DataVector](vecSize).map(i => {DataVector(nbOfVariables)})
    new UniversalGrid1D(gridGeometry,underlyingGrid)
  }
}

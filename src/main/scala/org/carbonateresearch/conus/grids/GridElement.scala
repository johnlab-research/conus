package org.carbonateresearch.conus.grids

import org.carbonateresearch.conus.common.CalculationParametersIOLabels
import org.carbonateresearch.conus.grids.universal.UniversalGrid1D

trait GridElement {
  val vecSize:Int
  def toString(timeStep:Int):String
  def toString(timeStep:Int,keys:CalculationParametersIOLabels*):String
  def setAtCell[T](key:Int,value:T, coordinates:Seq[Int]):Unit
  def setManyAtCell[T](keys:Seq[Int],values:Seq[T],coordinates:Seq[Int]):Unit
  def setForAllCells[T](key:Int,value:T):Unit
  def setManyForAllCells[T](keys:Seq[Int],values:Seq[T]):Unit
  def getValueAtCell[T](key:Int,coordinates:Seq[Int]):T
  def getValueForAllCells[T](key:Int):UniversalGrid1D
}

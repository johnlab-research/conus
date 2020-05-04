package org.carbonateresearch.conus.grids.universal

import org.carbonateresearch.conus.common.CalculationParametersIOLabels
import org.carbonateresearch.conus.grids.Grid
import breeze.linalg._

case class DataVector(vectorSize:Int) {
  val underlyingVector:DenseVector[Any] = DenseVector(vectorSize)
  def get[T](key:Int):T = {
    underlyingVector(key).asInstanceOf[T]
  }
  def set(key:Int,value:Any):Unit = {
    underlyingVector(key) = value
  }
  def setMany(keys:Seq[Int],values:Seq[Any]):Unit = {
    keys.foreach(k => underlyingVector(k) = values(keys.indexOf(k)))
  }
}

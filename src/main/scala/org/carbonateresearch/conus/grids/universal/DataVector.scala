package org.carbonateresearch.conus.grids.universal

import org.carbonateresearch.conus.common.CalculationParametersIOLabels
import org.carbonateresearch.conus.grids.Grid
import java.lang.System.lineSeparator
import breeze.linalg._

case class DataVector(vectorSize:Int,
                      coordinates:Seq[Int]) {
  private val EOL:String = lineSeparator()
  val underlyingVector:DenseVector[Any] = DenseVector.tabulate[Any](vectorSize) {i => 0}

  def get[T](key:Int):T = {
    underlyingVector(key).asInstanceOf[T]
  }
  def set(key:Int,value:Any):Unit = {
    underlyingVector(key) = value
  }
  def setMany(keys:Seq[Int],values:Seq[Any]):Unit = {
    keys.foreach(k => underlyingVector(k) = values(keys.indexOf(k)))
  }

  override def toString():String = {
    val cString:String = coordinates.head+coordinates.tail.map(x => ","+x.toString).foldLeft("")(_+_)
    cString+(0 until vectorSize).map(v => ","+underlyingVector(v).toString).foldLeft("")(_+_)+EOL
  }
}

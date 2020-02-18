package org.carbonateresearch.test

trait CalculationParametersIOLabelsT

trait DataContainerT
final case class DataContainer[T](value: T)  extends DataContainerT {
  def typeSafe[B](param:Parameter[B]): B = value.asInstanceOf[B]
}
object DataContainer{
  def apply(value:Int) = new DataContainer[Int](value)
  def apply(value:Double) = new DataContainer[Double](value)
  def apply(value:Float) = new DataContainer[Float](value)
  def apply(value:String) = new DataContainer[String](value)
}
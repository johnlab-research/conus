package org.carbonateresearch.test

import scala.reflect.ClassTag

case class CalculatedValues[T](values:Map[String,T]) {
  /*def get[T](thisKey: Parameter[T]): T = {
    values(thisKey.key) match {
      case value: T => value
    }
  }

  def get(thisKey: Parameter[Int]):Int = {
    values(thisKey.key).asInstanceOf[Int]}
  /*def get(thisKey: Parameter[String]):String = {
    values(thisKey.key).asInstanceOf[String]}*/
  def get(thisKey: Parameter[Double]):Double = {
    values(thisKey.key).asInstanceOf[Double]}*/

  def get(thisParameter: Parameter[T]): T = {

    values(thisParameter.key) match
    {
      case value: T => value
    }
  }
}

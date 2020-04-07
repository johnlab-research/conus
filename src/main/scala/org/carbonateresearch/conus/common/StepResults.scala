package org.carbonateresearch.conus.common
import shapeless.HMap
import shapeless._
import org.carbonateresearch.conus.common.{BiMapIS, ReturnSpecificValue}

case class StepResults(private val dataContainer:HMap[BiMapIS]) {
  def get[T](k:ModelVariable[T]): Option[T] = {
    implicit val mapKV = new BiMapIS[ModelVariable[T], T]
    dataContainer.get(k)
  }
  def add[T](k:ModelVariable[T],v:T):StepResults = {
    implicit val mapKV = new BiMapIS[ModelVariable[T], T]
    StepResults(this.dataContainer + (k -> v))
  }
}

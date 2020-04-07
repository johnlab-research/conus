package org.carbonateresearch.test
import org.carbonateresearch.conus.util.StepFunctionUtils.Step
import org.carbonateresearch.conus.common.BiMapIS

final case class ModelVariable[T](name: String, data:Map[Step,T]= Map[Step,T]()) extends CalculationParametersIOLabelsT {
  override def toString: String = name
  def key:String = name+this.getClass
  def apply(flag:Step):T = data(flag)
  def implicitBiMapIS:BiMapIS[ModelVariable[T],T] = new BiMapIS[ModelVariable[T],T]

}


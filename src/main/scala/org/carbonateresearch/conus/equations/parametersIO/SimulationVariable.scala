package org.carbonateresearch.conus.equations.parametersIO

import org.carbonateresearch.conus.util.StepFunctionUtils.Step

final case class SimulationVariable[T](name: String,
                                       unitName:String = "", defaultValue:Option[Number] = None,
                                       silent: Boolean = false,
                                       override val precision:Int = 2,
                                       implicit val data:Map[Step,T]= Map[Step,T]()) extends CalculationParametersIOLabels {
  override def toString: String = name
  override def unit: String = unitName
  def key:String = name+this.getClass
  def apply(stepNumber:Step):T = data(stepNumber)
}

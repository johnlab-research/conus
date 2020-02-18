package org.carbonateresearch.test
import RunTest.Step

final case class Parameter[T](name: String, data:Map[Step,T]= Map[Step,T]()) extends CalculationParametersIOLabelsT {
  override def toString: String = name
  def key:String = name+this.getClass
  def apply(flag:Step):T = data(flag)
  }
}


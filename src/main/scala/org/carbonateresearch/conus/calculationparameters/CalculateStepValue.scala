package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO._
import org.carbonateresearch.conus.calculationparameters.CalculateStepValueError
import spire.implicits._
import spire.math.Number


final case class CalculateStepValue(inputs: Option[List[CalculationParametersIOLabels]], output:CalculationParametersIOLabels, functionBlock: Option[List[Number] => Number]) extends CalculationParameters {

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    val numericalInputs = inputs match {
      case Some(l:List[CalculationParametersIOLabels]) => l.map(i => findValueFromLabel(i, step, previousResults))
      case None => List()
    }

    val result = functionBlock match {
      case Some(f) => f(numericalInputs)
      case None => Number(0)
    }

    val outputLabel = output match { // in case someone puts a Previous as an output label
      case i:Previous => i.input
      case _ => output
    }


    Map(step -> Map(outputLabel -> result))
  }

  def applying(function: Any):CalculateStepValue = {

    def newFunctionBlock(values: List[Number]) = {
      function match {
        case f:((Number) => Number) => f(values.head)
        case f:((Number, Number) => Number) => f(values.head, values(1))
      case f:((Number, Number, Number) => Number) => f(values.head, values(1), values(2))
      case f:((Number, Number, Number,Number) => Number) => f(values.head, values(1), values(2), values(3))
      }}
    CalculateStepValue(inputs = inputs, output = output, functionBlock = Option(newFunctionBlock))
  }

  def withParameters(newParameters:CalculationParametersIOLabels*): CalculateStepValue = {
    CalculateStepValue(inputs = Option(newParameters.toList), output = output, functionBlock = functionBlock)
  }

  private def findValueFromLabel(input:CalculationParametersIOLabels, step:Number, previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Number ={
    input match {
      case i:Previous => {
        if (step-i.offset >=0) {
          previousResults(step-i.offset)(i.input)
        } else {
          i.rule match{
            case TakeStepZeroValue => previousResults(Number(0))(i.input)
            case TakeCurrentStepValue =>previousResults(step)(i.input)
            case TakeSpecificValue(v) => v
            case TakeValueForLabel(l) => previousResults(step)(l)
          }
        }

      }
      case _ => previousResults(step)(input)
    }
  }

}

object CalculateStepValue {
  def apply(output: CalculationParametersIOLabels) = {
    new CalculateStepValue(inputs = None, output = output, functionBlock = None)
  }
}






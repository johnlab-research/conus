package org.carbonateresearch.conus.calculationparameters
import org.carbonateresearch.conus.calculationparameters.parametersIO._
import org.carbonateresearch.conus.common.{ModelResults}


final case class CalculateStepValue(override val inputs: Option[List[CalculationParametersIOLabels]], output:CalculationParametersIOLabels, override val functionBlock: Option[List[Double] => Double]) extends Calculator {

  override val outputs = List(output)

  override def calculate (step:Int,previousResults:ModelResults): ModelResults  = {

    val numericalInputs = inputs match {
      case Some(l:List[CalculationParametersIOLabels]) => l.map(i => findValueFromLabel(i, step, previousResults))
      case None => List()
    }

    val result = functionBlock match {
      case Some(f) => f(numericalInputs)
      case None => 0
    }

    val outputLabel = output match { // in case someone puts a Previous as an output label
      case i:Previous => i.input
      case _ => output
    }

    previousResults.addParameterResultAtStep(outputLabel,result.asInstanceOf[Double],step)
  }


  def applyingFunction(function: Any):CalculateStepValue = {

    def newFunctionBlock(values: List[Double]) = {
      function match {
        case f:((Double) => Double) => f(values.head)
        case f:((Double, Double) => Double) => f(values.head, values(1))
      case f:((Double, Double, Double) => Double) => f(values.head, values(1), values(2))
      case f:((Double, Double, Double,Double) => Double) => f(values.head, values(1), values(2), values(3))
      }}
    CalculateStepValue(inputs = inputs, output = output, functionBlock = Option(newFunctionBlock))
  }

  def withParameters(newParameters:CalculationParametersIOLabels*): CalculateStepValue = {
    CalculateStepValue(inputs = Option(newParameters.toList), output = output, functionBlock = functionBlock)
  }



  private def findValueFromLabel(input:CalculationParametersIOLabels, step:Int, previousModelResults:ModelResults): Double ={
    input match {
      case i:Previous => {
        if (step-i.offset >=0) {
          previousModelResults.getStepResult(step-i.offset,i.input)
        } else {
          i.rule match{
            case TakeStepZeroValue => previousModelResults.getStepResult(0,i.input)
            case TakeCurrentStepValue => previousModelResults.getStepResult(step,i.input)
            case TakeSpecificValue(v) => v
            case TakeValueForLabel(l) => previousModelResults.getStepResult(step,l)
          }
        }

      }
      case _ => previousModelResults.getStepResult(step,input)
    }
  }

}

object CalculateStepValue {
  def apply(output: CalculationParametersIOLabels) = {
    new CalculateStepValue(inputs = None, output = output, functionBlock = None)
  }
}






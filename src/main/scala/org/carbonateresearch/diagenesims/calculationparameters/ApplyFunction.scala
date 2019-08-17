package org.carbonateresearch.diagenesims.calculationparameters

import org.carbonateresearch.diagenesims.calculationparameters.parametersIO._
import spire.implicits._
import spire.math.Number



final case class ApplyFunction(inputs: List[CalculationParametersIOLabels], output: CalculationParametersIOLabels, functionBlock: List[Number] => Number) extends CalculationParameters {

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    val numericalInputs = inputs.map(i => findValueFromLabel(i, step, previousResults))
    val result = functionBlock(numericalInputs)


    Map(step -> Map(output -> result))
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
          }
        }

      }
      case _ => previousResults(step)(input)
    }
  }

}

object ApplyFunction {
  def apply(inputs: List[CalculationParametersIOLabels], outputs:List[CalculationParametersIOLabels], function: List[Number] => List[Number]) = {

    def functionBlock(values: List[Number]) = {
      function(values)(0)
    }
    new ApplyFunction(inputs = inputs,  output = outputs(0), functionBlock = functionBlock)
  }

  def apply(v1: CalculationParametersIOLabels, function: Number => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values(0))
    }
    new ApplyFunction(inputs = List(v1), output = output, functionBlock = functionBlock)
  }

  def apply(inputs: (CalculationParametersIOLabels, CalculationParametersIOLabels),
            function: (Number, Number) => Number,
            output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values(0), values(1))
    }
    new ApplyFunction(inputs = List(inputs._1,inputs._2), output = output, functionBlock = functionBlock)
  }

  def apply(v1: CalculationParametersIOLabels, v2: CalculationParametersIOLabels, v3: CalculationParametersIOLabels, function: (Number, Number, Number) => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values(0), values(1), values(2))
    }
    new ApplyFunction(inputs = List(v1,v2,v3), output = output, functionBlock = functionBlock)
  }

  def apply(v1: CalculationParametersIOLabels, v2: CalculationParametersIOLabels, v3: CalculationParametersIOLabels, v4: CalculationParametersIOLabels,function: (Number, Number, Number, Number) => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values(0), values(1), values(2), values(3))
    }
    new ApplyFunction(inputs = List(v1,v2,v3,v4), output = output, functionBlock = functionBlock)
  }

}




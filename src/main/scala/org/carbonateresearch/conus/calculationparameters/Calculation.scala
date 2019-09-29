package org.carbonateresearch.conus.calculationparameters

import org.carbonateresearch.conus.calculationparameters.parametersIO._
import spire.implicits._
import spire.math.Number



final case class Calculation(inputs: List[CalculationParametersIOLabels], output: CalculationParametersIOLabels, functionBlock: List[Number] => Number) extends CalculationParameters {

  override def calculate (step:Number,previousResults:Map[Number,Map[CalculationParametersIOLabels,Number]]): Map[Number,Map[CalculationParametersIOLabels ,Number]]  = {

    val numericalInputs = inputs.map(i => findValueFromLabel(i, step, previousResults))
    val result = functionBlock(numericalInputs)

    val outputLabel = output match { // in case someone puts a Previous as an output label
      case i:Previous => i.input
      case _ => output
    }


    Map(step -> Map(outputLabel -> result))
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

object Calculation {
  def apply(inputs: List[CalculationParametersIOLabels], outputs:List[CalculationParametersIOLabels], function: List[Number] => List[Number]) = {

    def functionBlock(values: List[Number]) = {
      function(values)(0)
    }
    new Calculation(inputs = inputs,  output = outputs(0), functionBlock = functionBlock)
  }

  def apply(v1: CalculationParametersIOLabels, function: Number => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values(0))
    }
    new Calculation(inputs = List(v1), output = output, functionBlock = functionBlock)
  }

  def apply(inputs: (CalculationParametersIOLabels, CalculationParametersIOLabels),
            function: (Number, Number) => Number,
            output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values.head, values(1))
    }
    new Calculation(inputs = List(inputs._1,inputs._2), output = output, functionBlock = functionBlock)
  }

  def apply(inputs:(CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels),
  function: (Number, Number, Number) => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values.head, values(1), values(2))
    }
    new Calculation(inputs = List(inputs._1,inputs._2, inputs._3), output = output, functionBlock = functionBlock)
  }


  def apply(inputs:(CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels, CalculationParametersIOLabels),
            function: (Number, Number, Number, Number) => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values.head, values(1), values(2), values(3))
    }
    new Calculation(inputs = List(inputs._1,inputs._2, inputs._3, inputs._4), output = output, functionBlock = functionBlock)
  }

  def apply(inputs:(CalculationParametersIOLabels, CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels, CalculationParametersIOLabels),
            function: (Number, Number, Number, Number, Number) => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values.head, values(1), values(2), values(3), values(4))
    }
    new Calculation(inputs = List(inputs._1,inputs._2, inputs._3, inputs._4, inputs._5), output = output, functionBlock = functionBlock)
  }

  def apply(inputs:(CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels, CalculationParametersIOLabels),
            function: (Number, Number, Number, Number, Number, Number) => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values.head, values(1), values(2), values(3), values(4), values(5))
    }
    new Calculation(inputs = List(inputs._1,inputs._2, inputs._3, inputs._4, inputs._5, inputs._6), output = output, functionBlock = functionBlock)
  }

  def apply(inputs:(CalculationParametersIOLabels,CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels, CalculationParametersIOLabels),
            function: (Number, Number, Number, Number, Number, Number, Number) => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values.head, values(1), values(2), values(3), values(4), values(5), values(6))
    }
    new Calculation(inputs = List(inputs._1,inputs._2, inputs._3, inputs._4, inputs._5, inputs._6, inputs._7), output = output, functionBlock = functionBlock)
  }

  def apply(inputs:(CalculationParametersIOLabels, CalculationParametersIOLabels,CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels, CalculationParametersIOLabels),
            function: (Number, Number, Number, Number, Number, Number, Number, Number) => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values.head, values(1), values(2), values(3), values(4), values(5), values(6), values(7))
    }
    new Calculation(inputs = List(inputs._1,inputs._2, inputs._3, inputs._4, inputs._5, inputs._6, inputs._7, inputs._8), output = output, functionBlock = functionBlock)
  }

  def apply(inputs:(CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels,CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels, CalculationParametersIOLabels),
            function: (Number, Number, Number, Number, Number, Number, Number, Number, Number) => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values.head, values(1), values(2), values(3), values(4), values(5), values(6), values(7), values(8))
    }
    new Calculation(inputs = List(inputs._1,inputs._2, inputs._3, inputs._4, inputs._5, inputs._6, inputs._7, inputs._8, inputs._9), output = output, functionBlock = functionBlock)
  }

  def apply(inputs:(CalculationParametersIOLabels,CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels,CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels,CalculationParametersIOLabels, CalculationParametersIOLabels, CalculationParametersIOLabels),
            function: (Number, Number, Number, Number, Number, Number, Number, Number, Number, Number) => Number, output:CalculationParametersIOLabels) = {

    def functionBlock(values: List[Number]) = {
      function(values.head, values(1), values(2), values(3), values(4), values(5), values(6), values(7), values(8), values(9))
    }
    new Calculation(inputs = List(inputs._1,inputs._2, inputs._3, inputs._4, inputs._5, inputs._6, inputs._7, inputs._8, inputs._9, inputs._10), output = output, functionBlock = functionBlock)
  }

}




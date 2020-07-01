package org.carbonateresearch.conus

import java.lang.System.lineSeparator

import org.carbonateresearch.conus.calibration.{InRange, LargerThan, SmallerThan, ValueEqualTo}
import org.carbonateresearch.conus.common._
import org.carbonateresearch.conus.util.StepFunctionUtils.StepFunction

import scala.util.{Failure, Success, Try}

final case class ModelVariable[T](override val name: String,
                                  initialValue:T,
                                  override val unitName:String = "",
                                  override val silent: Boolean = false,
                                  override val precision:Int = 2,
                                  ifNoValue:NoValueHandler = ReturnDefaultValue) extends CalculationParametersIOLabels {
val EOL = lineSeparator()
  override def toString: String = name
  override def defaultValue:Any = initialValue.asInstanceOf[Any]

  override def formatValueAsString(value:Any):String = {
    val typedValue:T = value.asInstanceOf[T]
    formatValue(precision,typedValue)
  }

  private def formatValue(precision:Int, value:T): String = {
    val pValue:Double =  1+precision.toDouble/10
    val formatString:String = "%"+ pValue.toString + "f"

    value match{
      case v:Double => formatString format value
      case v:Float => formatString format value
      case v:BigDecimal => formatString format value
      case _ => value.toString
    }
  }

  def apply(step:Step): T = {
    val calculatedStep = step.stepNumber-step.stepOffset
    val stepNumber:Int = if(calculatedStep<0){0}else{calculatedStep}

    val returnedValue:T = tryAccess(step,stepNumber) match {
      case Success(t:T) => t
      case Failure(e: Throwable) => {
        println("Exception "+e.toString+" reached in variable "+name+" at step "+step.stepNumber)
        tryAccess(step,0) match {
        case Success(z:T) => {
          println("Returning initial value instead"+EOL)
          z}
        case _ => {
          println("Returning default value instead"+EOL)
          initialValue}
      }
    }
    }
    returnedValue
  }

  private def tryAccess(step:Step,stepNumber:Int):Try[T] = {
     Try {
        step.grid.getVariableAtCellForTimeStep(this,step.coordinates)(stepNumber).asInstanceOf[T]
      }
    }

  def isLessThan(value:T)(implicit num:Numeric[T]):SmallerThan[T] = {
    SmallerThan(value:T,this)
  }

  def isLargerThan(value:T)(implicit num:Numeric[T]):LargerThan[T] = {
    LargerThan(value:T,this)
  }

  def isBetween(min:T, max:T)(implicit num:Numeric[T]):InRange[T] = {
    InRange(min, max,this)
  }

  def isEqualTo(value:T)(implicit num:Numeric[T]):ValueEqualTo[T] = {
    ValueEqualTo(value,this)
  }


  def =>>(f: StepFunction[T]):CalculationDescription[T] = {
    ApplyStepFunction(f).storeResultAs(this)
  }

}

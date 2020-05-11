package org.carbonateresearch.conus.common
import shapeless.HMap
import org.carbonateresearch.conus.common.Step
import org.carbonateresearch.conus.util.StepFunctionUtils.StepFunction
import java.lang.System.lineSeparator
import scala.util.{Try, Success,Failure}

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

  def =>>(f: StepFunction[T]):CalculationDescription[T] = {
    ApplyStepFunction(f).storeResultAs(this)
  }

}


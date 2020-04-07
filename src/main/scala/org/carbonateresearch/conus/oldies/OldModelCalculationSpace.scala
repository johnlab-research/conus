package org.carbonateresearch.conus.oldies

import java.lang.System.lineSeparator

import org.carbonateresearch.conus.common.{ModelCalibrationSet, ParallelCalculatorWithFuture}
import org.carbonateresearch.conus.equations.Calculator

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future
import scala.util.Success

final case class OldModelCalculationSpace(calculations:List[OldChainableCalculation], calibrationSets: List[ModelCalibrationSet], var results: List[OldSingleModelWithResults] = List()) {

  var resultsList = scala.collection.mutable.ListBuffer.empty[OldSingleModelWithResults].toList
  val EOL = lineSeparator()

  def next(nextCalculationParameter: Calculator): OldModelCalculationSpace = {
    OldModelCalculationSpace(calculations.map(cl => cl next nextCalculationParameter), List())
  }

  def defineMathematicalModelPerCell(calculationList: Calculator*): OldModelCalculationSpace = {
    val newCalculators:List[Calculator] = calculationList.toList
    val newChainableCalculations: List[OldChainableCalculation] = calculations.map(cl =>
      OldChainableCalculation(cl.ID, cl.steps, (cl.modelParameters++newCalculators).reverse))

    OldModelCalculationSpace(newChainableCalculations, List())
  }

  def calculationForEachCell(nextChainableCalculation: OldChainableCalculation): OldModelCalculationSpace = {
    OldModelCalculationSpace(calculations.map(cl => cl next nextChainableCalculation), List())
  }

  def next(nextModelCalculationSpace: OldModelCalculationSpace): OldModelCalculationSpace = {
    OldModelCalculationSpace(calculations.flatMap(cl => nextModelCalculationSpace.calculations.map(
      ncl => cl next ncl)), List())
  }

  def calibrationParameters(set:List[ModelCalibrationSet]) : OldModelCalculationSpace = {
    OldModelCalculationSpace(this.calculations,set)
  }

  def calibrationParameters(set:ModelCalibrationSet*) : OldModelCalculationSpace = {
    this.calibrationParameters(set.toList)
  }

  def size : Int = calculations.size

  def run: OldModelCalculationSpace = {
    val firstModels:List[Calculator] = calculations(0).modelParameters
    val parameterList = firstModels.map(c => c.outputs)
    println("----------------------------------------"+EOL+"RUN STARTED"+EOL+"----------------------------------------")

    @tailrec
    def checkErrors (parametersList:List[Calculator], currentString: String): String = {
      parametersList match  {
        case Nil => currentString
        case x::xs => checkErrors(xs,currentString+x.checkForError(xs))
      }
    }
    val errorsList :String = checkErrors(firstModels,"")

   if(errorsList == "") {
      println("No error detected, computing the following parameters in the model:" + EOL + parameterList.reverse.flatten.mkString(", ")+EOL+"----------------------------------------")
      implicit val ec = global
      val dispatcher = new ParallelCalculatorWithFuture
      //val dispatcher = new ParallelCalculatorWithMonix
      //val dispatcher = new ParallelCalculatorWithParCollection
      val newResults:Future[List[OldSingleModelWithResults]] = dispatcher.calculateModelsList(calculations)

      newResults.onComplete{case Success(results) => {
        this.results = results
        println(EOL+"----------------------------------------"+ EOL + "END OF RUN"+EOL+"----------------------------------------")
      }}
    }
    else {
      println(errorsList+EOL+"Impossible to initiate a run: Correct error(s) first")
    }
    //sequential
    this
  }



  def sequential = {

    val initialCount = calculations.size

    println("Now running sequentially for comparison")

    val t0 = System.nanoTime()
    val newResults = calculations.map(c => c.evaluate(t0))

    println(" ")
    println("-----------------------------------------")
    println("RUN TERMINATED")
    println("-----------------------------------------")


  }

  def handleResults(modelResults: List[OldSingleModelWithResults]): OldModelCalculationSpace = {
    OldModelCalculationSpace(calculations, calibrationSets, modelResults)
  }

  def calibrated():List[OldSingleModelWithResults] = {
    lazy val calibratedModelList:List[OldSingleModelWithResults] = if (calibrationSets.isEmpty) {
      resultsList
    } else {resultsList.filter(p => checkAllConditions(p))}

    def checkAllConditions(thisModel: OldSingleModelWithResults):Boolean = {
      val conditions:List[Boolean] = this.calibrationSets.map(cs => {
        cs.interval.contains(thisModel.finalResult(cs.calibrationParameters))
      })
      !conditions.contains(false)
    }

    println("Found " + calibratedModelList.size + " calibrated models:")
    calibratedModelList.map(r => r.summary + EOL).foreach(println)
    print("")

    calibratedModelList
  }

}

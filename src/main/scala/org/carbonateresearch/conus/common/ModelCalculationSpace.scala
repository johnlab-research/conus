package org.carbonateresearch.conus.common

import akka.actor.{Props}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import org.carbonateresearch.conus.calculationparameters.Calculator
import scala.collection.parallel.CollectionConverters._
import scala.annotation.tailrec
import java.lang.System.lineSeparator
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._
import scala.util.{Success, Failure}


final case class ModelCalculationSpace(calculations:List[ChainableCalculation], calibrationSets: List[ModelCalibrationSet], var results: List[SingleModelWithResults] = List()) {

  var resultsList = scala.collection.mutable.ListBuffer.empty[SingleModelWithResults].toList
  val EOL = lineSeparator()

  def next(nextCalculationParameter: Calculator): ModelCalculationSpace = {
    ModelCalculationSpace(calculations.map(cl => cl next nextCalculationParameter), List())
  }

  def defineMathematicalModelPerCell(calculationList: Calculator*): ModelCalculationSpace = {
    val newCalculators:List[Calculator] = calculationList.toList
    val newChainableCalculations: List[ChainableCalculation] = calculations.map(cl =>
      ChainableCalculation(cl.ID, cl.steps, (cl.modelParameters++newCalculators).reverse))

    ModelCalculationSpace(newChainableCalculations, List())
  }

  def calculationForEachCell(nextChainableCalculation: ChainableCalculation): ModelCalculationSpace = {
    ModelCalculationSpace(calculations.map(cl => cl next nextChainableCalculation), List())
  }

  def next(nextModelCalculationSpace: ModelCalculationSpace): ModelCalculationSpace = {
    ModelCalculationSpace(calculations.flatMap(cl => nextModelCalculationSpace.calculations.map(
      ncl => cl next ncl)), List())
  }

  def calibrationParameters(set:List[ModelCalibrationSet]) : ModelCalculationSpace = {
    ModelCalculationSpace(this.calculations,set)
  }

  def calibrationParameters(set:ModelCalibrationSet*) : ModelCalculationSpace = {
    this.calibrationParameters(set.toList)
  }

  def size : Int = calculations.size

  def run: ModelCalculationSpace = {
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
      val newResults:Future[List[SingleModelWithResults]] = dispatcher.calculateModelsList(calculations)

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

  def handleResults(modelResults: List[SingleModelWithResults]): ModelCalculationSpace = {
    ModelCalculationSpace(calculations, calibrationSets, modelResults)
  }

  def calibrated():List[SingleModelWithResults] = {
    lazy val calibratedModelList:List[SingleModelWithResults] = if (calibrationSets.isEmpty) {
      resultsList
    } else {resultsList.filter(p => checkAllConditions(p))}

    def checkAllConditions(thisModel: SingleModelWithResults):Boolean = {
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



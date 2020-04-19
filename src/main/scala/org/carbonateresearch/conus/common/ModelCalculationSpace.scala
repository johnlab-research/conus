package org.carbonateresearch.conus.common

import akka.actor.Props

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import org.carbonateresearch.conus.common.Calculator

import scala.collection.parallel.CollectionConverters._
import scala.annotation.tailrec
import java.lang.System.lineSeparator

import org.carbonateresearch.conus.grids.Grid
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}


final case class ModelCalculationSpace(grid:Grid, calculations:List[ChainableCalculation], calibrationSets: List[Calibrator], var results: List[EvaluatedModel] = List()) {

  var resultsList = scala.collection.mutable.ListBuffer.empty[EvaluatedModel].toList
  val EOL = lineSeparator()

  def next(nextCalculationParameter: Calculator): ModelCalculationSpace = {
    ModelCalculationSpace(grid,calculations.map(cl => cl next nextCalculationParameter), List())
  }

  def defineMathematicalModelPerCell(calculationList: Calculator*): ModelCalculationSpace = {
    val newCalculators:List[Calculator] = calculationList.toList
    val newChainableCalculations: List[ChainableCalculation] = calculations.map(cl =>
      ChainableCalculation(cl.ID, cl.steps, (cl.modelParameters++newCalculators).reverse))

    ModelCalculationSpace(grid,newChainableCalculations, List())
  }

  // Is the below still needed?
  def calculationForEachCell(nextChainableCalculation: ChainableCalculation): ModelCalculationSpace = {
    ModelCalculationSpace(grid,calculations.map(cl => cl next nextChainableCalculation), List())
  }

  def next(nextModelCalculationSpace: ModelCalculationSpace): ModelCalculationSpace = {
    ModelCalculationSpace(grid,calculations.flatMap(cl => nextModelCalculationSpace.calculations.map(
      ncl => cl next ncl)), List())
  }

  def calibrationParameters(set:List[Calibrator]) : ModelCalculationSpace = {
    ModelCalculationSpace(grid,this.calculations,set)
  }

  def calibrationParameters(set:Calibrator*) : ModelCalculationSpace = {
    this.calibrationParameters(set.toList)
  }

  def size : Int = calculations.size

  def run: ModelCalculationSpace = {
    val timeout = Timeout(35.minutes)
    val firstModels:List[Calculator] = calculations(0).modelParameters
    //val parameterList = firstModels.map(c => c.outputs)
    println("----------------------------------------"+EOL+"RUN STARTED"+EOL+"----------------------------------------")
/*
    @tailrec
    def checkErrors (parametersList:List[Calculator], currentString: String): String = {
      parametersList match  {
        case Nil => currentString
        case x::xs => checkErrors(xs,currentString+x.checkForError(xs))
      }
    }
    val errorsList :String = checkErrors(firstModels,"")

   if(errorsList == "") {*/
     //println("No error detected, computing the following parameters in the model:" + EOL + parameterList.reverse.flatten.mkString(", ")+EOL+"----------------------------------------")
     implicit val ec = global
      val dispatcher = new CalculationDispatcherWithFuture
      //val dispatcher = new ParallelCalculatorWithMonix
      //val dispatcher = new CalculationDispatcherWithParCollection
     //val dispatcher = new CalculationDispatcherSequential
      val newResults:Future[List[EvaluatedModel]] = dispatcher.calculateModelsList(calculations)

      newResults.onComplete{
        case Success(results) => {
        this.results = results
        println(EOL+"----------------------------------------"+ EOL + "END OF RUN"+EOL+"----------------------------------------")
          println("DETAILED RESULTS:")
          results.map(r => println(r.completeModelResultsString))
          ExcelIO.writeExcel(results,"Users/cedric/CONUS/")
      }
        case Failure(failure) => {
          println("Model has failed to run: "+failure.getMessage)}
      }

   //sequential
    this
  }



  def sequential = {

    val initialCount = calculations.size

    println("Now running sequentially for comparison")

    val t0 = System.nanoTime()
    val newResults = calculations.map(c => c.evaluate(t0))

    newResults.map(r => println(r.completeModelResultsString))

    println(" ")
    println("-----------------------------------------")
    println("RUN TERMINATED")
    println("-----------------------------------------")


  }

  def handleResults(modelResults: List[EvaluatedModel]): ModelCalculationSpace = {
    ModelCalculationSpace(grid,calculations, calibrationSets, modelResults)
  }

  def calibrated():List[EvaluatedModel] = {
    lazy val calibratedModelList:List[EvaluatedModel] = if (calibrationSets.isEmpty) {
      resultsList
    } else {resultsList.filter(p => checkAllConditions(p))}

    def checkAllConditions(thisModel: EvaluatedModel):Boolean = {
      val conditions:List[Boolean] = this.calibrationSets.map(cs => {
        //cs.interval.contains(thisModel.finalResult(cs.calibrationParameters))
        false
      })
      !conditions.contains(false)
    }

    println("Found " + calibratedModelList.size + " calibrated models:")
    calibratedModelList.map(r => r.summary + EOL).foreach(println)
    print("")

    calibratedModelList
  }

}



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

import org.carbonateresearch.conus.grids.GridFactory
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import org.carbonateresearch.conus.grids._

final case class ModelCalculationSpace(models: List[SingleModel] = List(),
                                       modelName:String,
                                       calibrationSets: List[Calibrator] = List(),
                                       var results: List[SingleModelResults] = List()) {

  var resultsList = scala.collection.mutable.ListBuffer.empty[SingleModelResults].toList
  private val EOL = lineSeparator()
  private val modelFolder:String = System.getProperty("user.home")+"/Conus/"+modelName

  def calibrationParameters(set:List[Calibrator]) : ModelCalculationSpace = {
    this.copy(calibrationSets=set)
  }

  def calibrationParameters(set:Calibrator*) : ModelCalculationSpace = {
    this.calibrationParameters(set.toList)
  }

  def size : Int = models.size

  def run: ModelCalculationSpace = {
    val timeout = Timeout(35.minutes)
    val firstModels:List[Calculator] = models(0).calculations
    models.foreach(m=> println(m.ID))
    println("----------------------------------------"+EOL+"RUN STARTED"+EOL+"----------------------------------------")

     implicit val ec = global
      val dispatcher = new CalculationDispatcherWithFuture
      //val dispatcher = new ParallelCalculatorWithMonix
      //val dispatcher = new CalculationDispatcherWithParCollection
     //val dispatcher = new CalculationDispatcherSequential
      val newResults:Future[List[SingleModelResults]] = dispatcher.calculateModelsList(models)

      newResults.onComplete{
        case Success(results) => {
        this.results = results
        println(EOL+"----------------------------------------"+ EOL + "END OF RUN"+EOL+"----------------------------------------")
         ExcelIO.writeExcel(results,modelFolder)
      }
        case Failure(failure) => {
          println("Model has failed to run: "+failure.getMessage)}
      }

    this
  }

  def handleResults(modelResults: List[SingleModelResults]): ModelCalculationSpace = {
    ModelCalculationSpace(models, modelName,calibrationSets, modelResults)
  }

  def calibrated():List[SingleModelResults] = {
    lazy val calibratedModelList:List[SingleModelResults] = if (calibrationSets.isEmpty) {
      resultsList
    } else {resultsList.filter(p => checkAllConditions(p))}

    def checkAllConditions(thisModel: SingleModelResults):Boolean = {
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



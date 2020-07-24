package org.carbonateresearch.conus.simulators

import java.nio.file.{InvalidPathException, Paths}
import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import org.carbonateresearch.conus.BasicSimulator
import org.carbonateresearch.conus.IO.ExcelEncoder
import org.carbonateresearch.conus.common.{ModelCalculationSpace, ModelResults, SingleModelResults}
import org.carbonateresearch.conus.dispatchers.{runBehaviour,RunMultipleModels}
import org.carbonateresearch.conus.dispatchers.{LoggerType, ModelDispatcherAkka}
import org.carbonateresearch.conus.notebook.AlmondDisplay

import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future

object AkkaCentralSimulatorActor {
  val system: ActorSystem[runBehaviour] = ActorSystem(ModelDispatcherAkka(),"CoNuS")
  var runnedModels:Map[ModelCalculationSpace,List[SingleModelResults]] = Map()
  var PID = 0
  var baseDirectory:String = System.getProperty("user.home")+"/Conus/"
  var autoSave:Boolean = false
  var simulatorInterface:Simulator = new BasicSimulator

  trait Notebook
  case class Almond(cellID:String) extends Notebook
  case object Zepplin extends Notebook


  def evaluate(models: ModelCalculationSpace, resultsLogger:LoggerType): Unit = {
    PID += 1
    system ! RunMultipleModels(models,PID,autoSave,resultsLogger)
  }



  def save(key:ModelCalculationSpace): Unit = {
    val modelToWrite = runnedModels(key)
    val dateAndTime:String = new SimpleDateFormat("/yyyy-MM-dd-hh-mm-ss").format(new Date)
    val path = baseDirectory+modelToWrite.head.modelName+dateAndTime+"/"
    implicit val ec = global
    runnedModels(key).map(r => Future({
      val encoder = new ExcelEncoder
      encoder.writeExcel(List(r),path)})
    )
  }

  def updateModelList(key:ModelCalculationSpace,models: List[SingleModelResults]): Unit = {
    runnedModels += (key->models)
  }

  def setUserDirectory(proposedPath:String): Unit = {
    val valid:Boolean = {
      try {
        Paths.get(proposedPath)
        true
      }
    catch {
      case ex: InvalidPathException =>  false
    }}

if(valid){
  baseDirectory = proposedPath+"/conus"
} else {println("Invalid pathname")}
    }

  def apply(key:ModelCalculationSpace):ModelResults = getResults(key)


  def getResults(key:ModelCalculationSpace):ModelResults = {
    if (runnedModels.isDefinedAt(key)) {
      val theSize = runnedModels(key).size
      ModelResults(runnedModels(key))} else {
      ModelResults(List())
    }
  }

  def Almond_Display(implicit myKernel:almond.api.JupyterApi) = {
    val id = java.util.UUID.randomUUID().toString
    val display = new AlmondDisplay
  display.registerDisplays
  }
}

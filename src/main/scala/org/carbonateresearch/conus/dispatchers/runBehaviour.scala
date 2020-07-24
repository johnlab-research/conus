package org.carbonateresearch.conus.dispatchers

import akka.actor.typed.ActorRef
import org.carbonateresearch.conus.common.{ModelCalculationSpace, SingleModel, SingleModelResults}

sealed trait runBehaviour
final case class RunMultipleModels(modelsToRun: ModelCalculationSpace, PID:Int, autoSave:Boolean,updater:LoggerType) extends runBehaviour
final case class RunSingleModel(theModel: SingleModel, startTime:Double, runName:String, replyTo: ActorRef[ResultsSingleRun], autoSave:Boolean, logger:ActorRef[runBehaviour],loggerType: LoggerType) extends runBehaviour
final case class ResultsSingleRun(theResults: SingleModelResults,logger:ActorRef[runBehaviour], loggerType: LoggerType) extends runBehaviour
final case class WriteableModelResults(theResults: SingleModelResults, runName:String) extends runBehaviour
final case class AlmondPrintableMessage(theMessage:String, kernel: almond.api.JupyterApi, cellID:String, logger: ActorRef[runBehaviour]) extends runBehaviour
final case class ConsolePrintableMessage(theMessage:String,logger:ActorRef[runBehaviour]) extends runBehaviour
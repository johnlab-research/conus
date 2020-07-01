/*
 * Copyright © 2020 by Cédric John.
 *
 * This file is part of CoNuS.
 *
 * CoNuS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * CoNuS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CoNuS. If not, see <http://www.gnu.org/licenses/>.
 */

package org.carbonateresearch.conus

import org.carbonateresearch.conus.dispatchers.CalculationDispatcherAkka
import akka.actor.typed.ActorSystem
import org.carbonateresearch.conus.common.{ModelCalculationSpace, ModelResults, SingleModelResults}
import java.nio.file.InvalidPathException
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.Date

import org.carbonateresearch.conus.IO.ExcelEncoder
import org.carbonateresearch.conus.notebook.Almond_display
import jupyter.Displayers
import almond.api.JupyterApi.UpdatableResults
import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContext, Future}


object Simulator {
  val system: ActorSystem[CalculationDispatcherAkka.runBehaviour] = ActorSystem(CalculationDispatcherAkka(),"CoNuS")
  var runnedModels:Map[ModelCalculationSpace,List[SingleModelResults]] = Map()
  var PID = 0
  var baseDirectory:String = System.getProperty("user.home")+"/Conus/"
  var autoSave:Boolean = false
  private var notebook:Option[Notebook] = None

  trait Notebook
  case class Almond(cellID:String) extends Notebook
  case object Zepplin extends Notebook

  def evaluate(models: ModelCalculationSpace): Unit = {
    if (runnedModels.isDefinedAt(models)) {runnedModels.remove(models)}
    PID += 1
    system ! CalculationDispatcherAkka.RunMultipleModels(models,PID,autoSave)
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
    runnedModels.addOne(key,models)
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
    if (runnedModels.isDefinedAt(key)) {ModelResults(runnedModels(key))} else {
      ModelResults(List())
    }
  }

  def almond_display = {
    val id = java.util.UUID.randomUUID().toString
    val display = new Almond_display
  display.registerDisplays
    notebook = Some(Almond(id))
  }
}

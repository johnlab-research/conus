package org.carbonateresearch.conus.common

import akka.actor.{Actor, Props, _}
import akka.pattern.ask
import akka.util.Timeout
import org.carbonateresearch.conus.oldies.{OldChainableCalculation, OldModelCalculationSpace, OldSingleModelWithResults}

import scala.compat.Platform.EOL
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success

class ParrallelModellerDispatcherActor extends Actor {
  var initialCount:Int = 0
  var t0 = System.nanoTime()
  var owner:OldModelCalculationSpace = null
  var resultsList = scala.collection.mutable.ListBuffer.empty[OldSingleModelWithResults]
  val collector:ActorRef = context.actorOf(Props[ParrallelModellerCollectorActor], name="Collector")

  override def receive = {
    case modelSpace: OldModelCalculationSpace => {
      owner = modelSpace
      val modelsList: List[OldChainableCalculation] = modelSpace.calculations
      initialCount = modelsList.size

      println("Initiating a run on " + modelsList.size.toString + " models.")

      implicit val timeout = Timeout(30.minutes)

      t0 = System.nanoTime()
      modelsList.map(m => context.actorOf(Props(new ParrallelSingleModelRunnerActor)) ! m)
    }
    case newResult: OldSingleModelWithResults => {
      implicit val ec = global
      resultsList += newResult
      val modelData = newResult.summary + EOL
      val t1 = System.nanoTime()
      val elapsedTime: Double = (t1 - t0)
      val elapsedTimeStr: String = getHoursMinuteSeconds(elapsedTime)
      val percentCompleted = ((resultsList.size.toDouble / initialCount.toDouble) * 100).ceil
      val predictedTime = elapsedTime / percentCompleted * (100 - percentCompleted)
      val predictedTimeStr: String = getHoursMinuteSeconds(predictedTime)

      if (resultsList.size == initialCount) {
        val runStatistics = "-> 100% completed in " + elapsedTimeStr + "." + EOL
        println(modelData + runStatistics)
        owner.resultsList = resultsList.toList
        owner.calibrated()
        //actorSystem.terminate()
      }
      else {
        val runStatistics = "-> " + percentCompleted + "% completed in " + elapsedTimeStr + ". Predicted time remaining: " + predictedTimeStr + "." + EOL
       // println(modelData + runStatistics)
      }

    }
    case _ => println("Sample type not handled by Modeler")
  }

  def getHoursMinuteSeconds(nannoseconds:Double): String = {
    val totalTimeInSeconds:Double = nannoseconds/10E8
    val hours:Int = (totalTimeInSeconds/(60*60)).toInt
    val secondsRemainingForMinutes =  (totalTimeInSeconds % (60*60)).toInt
    val minutes:Int =(secondsRemainingForMinutes/60).toInt
    val seconds:Int =(secondsRemainingForMinutes % 60).toInt

   val  timeString:String = if(hours>0 && minutes>0){hours+" hours, " + minutes + " minutes, and " + seconds + " seconds"}
   else if(hours==0 && minutes>0){ minutes + " minutes, and " + seconds + " seconds"}
   else {seconds + " seconds"}
    timeString
  }
}

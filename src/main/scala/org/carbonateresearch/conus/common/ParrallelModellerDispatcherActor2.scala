package org.carbonateresearch.conus.common

import akka.actor.{Actor, Props, _}
import akka.pattern.ask
import akka.util.Timeout
import org.carbonateresearch.conus.DiageneSim.actorSystem

import scala.compat.Platform.EOL
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success

class ParrallelModellerDispatcherActor2 extends Actor {
  var initialCount:Int = 0
  var resultsList = scala.collection.mutable.ListBuffer.empty[SingleModelWithResults]
  val collector:ActorRef = context.actorOf(Props[ParrallelModellerCollectorActor], name="Collector")

  override def receive = {
    case modelSpace: ModelCalculationSpace => {
      val modelsList: List[ChainableCalculation] = modelSpace.calculations
      val initialCount = modelsList.size

      println("Initiating a run on " + modelsList.size.toString+ " models.")

      implicit val timeout = Timeout(30 minutes)

      val t0 = System.nanoTime()
      val future:List[Future[Any]] = modelsList.map(m =>
        context.actorOf(Props(new ParrallelModellerRunnerActor)) ? m )

      implicit val ec = global

      future.foreach(f =>
        f onComplete {
          case Success(model) => {
            model match {
              case m:SingleModelWithResults => {
                resultsList += m
                val modelData = m.summary+EOL

                val t1 = System.nanoTime()
                val elapsedTime:Double = (t1 - t0)

                val elapsedTimeStr:String = getHoursMinuteSeconds(elapsedTime)
                val percentCompleted = ((resultsList.size.toDouble/initialCount.toDouble)*100).ceil
                val predictedTime = elapsedTime/percentCompleted*(100-percentCompleted)
                val predictedTimeStr:String = getHoursMinuteSeconds(predictedTime)

                if(resultsList.size == initialCount){
                  val runStatistics = "-> 100% completed in "+elapsedTimeStr+"."+ EOL
                  println(modelData+runStatistics)
                  modelSpace.resultsList = resultsList.toList
                  modelSpace.calibrated()
                  actorSystem.terminate()
                }
                else {
                  val runStatistics = "-> "+percentCompleted+"% completed in "+ elapsedTimeStr+". Predicted time remaining: "+predictedTimeStr+"."+ EOL
                  println(modelData+runStatistics)
                }

                }
            }}
        })

    }
    case _       => println("Sample type not handled by Modeler")
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

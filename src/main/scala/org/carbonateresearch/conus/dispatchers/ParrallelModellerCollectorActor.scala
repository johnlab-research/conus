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

package org.carbonateresearch.conus.dispatchers

import akka.actor.Actor

abstract class ParrallelModellerCollectorActor extends Actor {
  /*
  var resultsList = scala.collection.mutable.ListBuffer.empty[OldSingleModelWithResults]

  override def receive = {

    case modelsList: List[OldChainableCalculation] => {
      val initialCount = modelsList.size
      println("Now dispatching a modelsList of size " + modelsList.size.toString)

      implicit val timeout = Timeout(5.seconds)

      val future:List[Future[Any]] = modelsList.map(m =>
        context.actorOf(Props(new ParrallelSingleModelRunnerActor)) ? m )

      implicit val ec = global

      future.foreach(f =>
      f onComplete {
        case Success(model) => {
          model match {
            case m:OldSingleModelWithResults => {
          resultsList += m
          println(resultsList)}
            }}
      })

      sender ! future

      println("Dispatcher should have the results")

    }
    case s:String       => {
      println(s)
      sender ! List("This is a response from the Collector", "Did you receive it?")
    }
    case _       => println("Sample type not handled by Modeler")
  }
  */
}

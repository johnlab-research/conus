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

import java.lang.System.lineSeparator
import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.Behavior
import org.carbonateresearch.conus.IO.ExcelEncoder
import org.carbonateresearch.conus.simulators.AkkaCentralSimulatorActor

import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future


object FileWriterDispatcherAkka {

  def apply(): Behavior[WriteableModelResults] = Behaviors.setup { context =>

    Behaviors.receive { (context, message) => {
      val model = message.theResults
      val path = AkkaCentralSimulatorActor.baseDirectory + model.modelName + message.runName
      val encoder = new ExcelEncoder
      implicit val ec = global
      val write = Future {
        encoder.writeExcel(List(model), path)
      }

      write.onComplete{
      f => Behaviors.stopped}
      Behaviors.same
    }


    }
  }
}



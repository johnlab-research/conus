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

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.Behavior
import org.carbonateresearch.conus.IO.ExcelEncoder
import org.carbonateresearch.conus.common.SingleModelResults
import org.carbonateresearch.conus.simulators.AkkaCentralSimulatorActor

import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future

object SingleModelRunnerAkka  {

  def apply(): Behavior[RunSingleModel] = Behaviors.setup { context =>

    Behaviors.receive { (context, message) => {
      val result:SingleModelResults = message.theModel.evaluate(message.startTime)

      if(message.autoSave){
      val path = AkkaCentralSimulatorActor.baseDirectory + result.modelName + "/" + message.runName
      val encoder = new ExcelEncoder
      implicit val ec = global
      Future(encoder.writeExcel(List(result),path))
      }
      message.replyTo ! ResultsSingleRun(result,message.logger, message.loggerType)
    }

      Behaviors.stopped
    }
  }

}

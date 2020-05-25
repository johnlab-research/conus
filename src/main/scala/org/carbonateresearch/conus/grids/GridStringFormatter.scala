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

package org.carbonateresearch.conus.grids
import java.lang.System.lineSeparator
class GridStringFormatter {

private val EOL = lineSeparator()

  def printAsGrid(rawString:String): String = {
    val rows:Array[String] = rawString.split(EOL)
    val headers:Array[String] = rows.head.split(",").map(h => " | "+h)
    val data:Array[Array[String]] = rows.tail.map(str => str.split(","))
    val headerSizes:Array[Int] = headers.map(h => h.size)
    val interlinesizes:Int = headerSizes.sum+1
    val interline:String = "-"*(interlinesizes)+EOL

    val gridString = {
      interline +
      headers.foldLeft("")(_+_) + " |" + EOL + interline +
      data.map(da => {
        (0 until headers.size).map(i => "|"+da(i).take(headerSizes(i)-1)).foldLeft("")(_+_) + "|" + EOL + interline
      }).foldLeft("")(_+_)
    }
gridString
  }
}

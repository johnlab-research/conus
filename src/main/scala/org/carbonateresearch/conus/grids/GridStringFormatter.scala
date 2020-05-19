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

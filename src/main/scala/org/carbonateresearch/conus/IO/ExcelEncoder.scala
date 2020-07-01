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

package org.carbonateresearch.conus.IO

import java.io.{File, FileOutputStream}
import java.lang.System.lineSeparator
import java.text.SimpleDateFormat
import java.util.Date
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}
import org.carbonateresearch.conus.common.{CalculationParametersIOLabels, SingleModelResults}


class ExcelEncoder{

  private val EOL = lineSeparator()

  def writeExcel(models: List[SingleModelResults], fullPathName:String): Unit ={
    val dir = new File(fullPathName)
    val successful = dir.mkdirs
    println("Writing results to folder '"+fullPathName+"'")

    models.foreach(m => {
      val rsme:String = m.rsme match {
        case Some(v) => f"RSME[$v%1.5f]_Model"
        case _ =>"RSME:[N/A]_Model"
      }
      writeIndividualModel(m,fullPathName+rsme+m.ID+".xlsx")})
  }

  private def checkedCalibrated(isCalibrated:Boolean):String = if (isCalibrated){"/Calibrated_Model"} else{"/Uncalibrated_Model"}

  private def writeIndividualModel(model:SingleModelResults,filename:String):Unit = {
    val workbook = new XSSFWorkbook
    val formatter = new DataFormatter()

    // Add a sheet to the workbook
    val sheet1 = workbook.createSheet("Model results")
    val sheet2:XSSFSheet = workbook.createSheet("Initial model parameters")

    formatInitialConditions(model,sheet2)
    formatModelData(model,sheet1)

    // Save the updated workbook as a new file
    val fos = new FileOutputStream(filename)
    workbook.write(fos)
    workbook.close()
  }

  private def formatInitialConditions(model:SingleModelResults, icSheet:XSSFSheet)={
    val row = icSheet.createRow(0)
    val cell1 = row.createCell(0)
    cell1.setAsActiveCell()
    cell1.setCellValue("PARAMETER")
    val cell2 = row.createCell(1)
    cell2.setAsActiveCell()
    cell2.setCellValue("VALUE")
    val cell3 = row.createCell(2)
    cell3.setAsActiveCell()
    cell3.setCellValue("UNITS")
    val cell4 = row.createCell(3)
    cell4.setAsActiveCell()
    cell4.setCellValue("COORDINATES")
    val cell5 = row.createCell(4)
    cell5.setAsActiveCell()
    cell5.setCellValue("RSME")

    val initialConditions:List[(CalculationParametersIOLabels, Any, Seq[Int])] =
      model.initialConditions.flatMap(ic => ic.values.map(icv => (ic.variable,icv._1,icv._2)))
    val numberOfRows = initialConditions.indices
    val modelID = model.ID

    numberOfRows.foreach(r => {
      val row = icSheet.createRow(r+1)
      val cell1 = row.createCell(0)
      cell1.setAsActiveCell()
      cell1.setCellValue(initialConditions(r)._1.toString)

      val cell2 = row.createCell(1)
      cell2.setAsActiveCell()
      cell2.setCellValue(initialConditions(r)._2.toString)

      val cell3 = row.createCell(2)
      cell3.setAsActiveCell()
      cell3.setCellValue(initialConditions(r)._1.unitName)

      val cell4 = row.createCell(3)
      cell4.setAsActiveCell()
      cell4.setCellValue(coordinatesAsString(initialConditions(r)._3))

      val rsme:String = model.rsme match {
        case Some(v) => v.toString
        case _ => "NA"
      }

      val cell5 = row.createCell(4)
      cell5.setAsActiveCell()
      cell5.setCellValue(rsme)
    })


  }

  private def coordinatesAsString(coord:Seq[Int]):String ={
      coord.length match {
        case 0 => "All cells"
        case _ => {
          coord.map(c => c.toString+",").foldLeft("")(_+_).dropRight(1)
        }
      }
    }

  private def formatModelData(model:SingleModelResults, dataSheet:XSSFSheet):Unit ={
    val rawString = model.theGrid.toString
    val rows:Array[String] = rawString.split(EOL)
    val nbRows:Int = rows.length
    val data:Array[Array[String]] = rows.map(str => str.split(","))
    val rowIndex = rows.indices
    val colIndex = data(0).indices
    rowIndex.foreach(ri => {
      val row = dataSheet.createRow(ri)
      colIndex.foreach(ci => {
        val cell = row.createCell(ci)
        cell.setAsActiveCell()
        cell.setCellValue(data(ri)(ci))
      })
    })
  }
}

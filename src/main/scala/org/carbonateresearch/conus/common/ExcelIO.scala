package org.carbonateresearch.conus.common
import org.apache.poi.ss.usermodel.{DataFormatter, WorkbookFactory}
import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}
import java.io.{File, FileOutputStream}
import java.text.SimpleDateFormat

import scala.jdk.CollectionConverters
import java.lang.System.lineSeparator
import java.util.{Calendar, Date}

case object ExcelIO{

  private val EOL = lineSeparator()

  def writeExcel(models: List[SingleModelResults], pathName:String): Unit ={
    val dateAndTime:String = new SimpleDateFormat("/yyyy-MM-dd-hh-mm-ss").format(new Date)
    val fullPathName = pathName+dateAndTime
    val dir = new File(fullPathName)
    val successful = dir.mkdirs
    if (successful) {
      println("Writing results to folder '"+fullPathName+"'")
    models.foreach(m => writeIndividualModel(m,fullPathName+"/Model"+m.ID+".xlsx"))}
  }

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
    cell3.setCellValue("UNIT")

    val initialConditions:List[(CalculationParametersIOLabels,Any)] = model.initialConditions.map(ic => (ic.variable,ic.value))
    val numberOfRows = initialConditions.indices

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
    })
  }

  private def formatModelData(model:SingleModelResults, dataSheet:XSSFSheet):Unit ={
    val rawString = model.theGrid.toString
    val rows:Array[String] = rawString.split(EOL)
    val nbRows:Int = rows.length
    val data:Array[Array[String]] = rows.map(str => str.split(","))
    println(data(0)(0).toString)
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

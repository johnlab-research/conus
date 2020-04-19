package org.carbonateresearch.conus.common
import org.apache.poi.ss.usermodel.{WorkbookFactory, DataFormatter}
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.{File, FileOutputStream}
import scala.jdk.CollectionConverters

case object ExcelIO{
  def writeExcel(models: List[EvaluatedModel],filename:String): Unit ={
    models.foreach(m => writeIndividualModel(m,filename+"/Model"+m.ID+".xlsx"))
  }

  private def writeIndividualModel(model:EvaluatedModel,filename:String):Unit = {
    val workbook = new XSSFWorkbook
    val formatter = new DataFormatter()

    // Add a sheet to the workbook
    val sheet1 = workbook.createSheet("Model results")
    val sheet2 = workbook.createSheet("Initial model parameters")

    val row = sheet2.createRow(0)
    val cell1 = row.createCell(0)
    cell1.setAsActiveCell()
    cell1.setCellValue("PARAMETER")
    val cell2 = row.createCell(1)
    cell2.setAsActiveCell()
    cell2.setCellValue("VALUE")
    val cell3 = row.createCell(2)
    cell3.setAsActiveCell()
    cell3.setCellValue("UNIT")

    val headers:List[CalculationParametersIOLabels] = model.results.getModelVariablesForStep(0)

    val numberOfRows = (0 until headers.size)

    numberOfRows.foreach(r => {
    val row = sheet2.createRow(r+1)
    val cell1 = row.createCell(0)
    cell1.setAsActiveCell()
      cell1.setCellValue(headers(r).toString())

      val cell2 = row.createCell(1)
      cell2.setAsActiveCell()
      cell2.setCellValue(model.results.getStepResult(0,headers(r)).toString)

      val cell3 = row.createCell(2)
      cell3.setAsActiveCell()
      cell3.setCellValue(headers(r).unitName)
    })
    val filename2 = "Model"+model.ID+".xlsx"
    // Save the updated workbook as a new file
    val fos = new FileOutputStream(filename2)
    workbook.write(fos)
    workbook.close()
  }
}

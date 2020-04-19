package org.carbonateresearch.conus.common
import java.lang.System.lineSeparator

import Console.{BLUE, CYAN, GREEN, MAGENTA, RED, RESET, UNDERLINED, WHITE, YELLOW, YELLOW_B}
import scala.concurrent.Future

trait CalculationDispatcher {
def calculateModelsList(models:List[ChainableCalculation]):Future[List[EvaluatedModel]] = {???}
val typeOfDispatcher:String = "Not specified"
  def outputString(models:List[ChainableCalculation]):String = {
    val EOL = lineSeparator()
    val nbModels = models.size
    val model = models.head
    val nbOperation = model.modelParameters.size
    val nbSteps = model.steps.size
    val totalNumberOperations = (nbModels*nbSteps*nbOperation)
    val totNbOperationString:String = totalNumberOperations.toString()
    val strLen = totNbOperationString.length
    val remainingChar = strLen%3
    val groupsOfThrees:Int = ((strLen-remainingChar)/3) - {if(remainingChar == 0){1}else{0}}

    val positions = (1 to groupsOfThrees).reverse.toList

    def placeTick(currentString: String, pos: List[Int]): String = {
      pos match {
        case Nil => {currentString}
        case x::xs => {placeTick(currentString, xs).patch(strLen-(x*3), "'", 0)}
      }
    }

    val totNbOperationStringFormatted = placeTick(totNbOperationString,positions)

    s"${RESET}${MAGENTA}${UNDERLINED}RUN DATA${RESET}" + EOL +
      s"${RESET}${YELLOW}Total number of models:${RESET} ${WHITE}" + nbModels.toString + EOL +
      s"${RESET}${YELLOW}Number of steps per model:${RESET}${WHITE} " + nbSteps + EOL +
      s"${RESET}${YELLOW}Number of operation per step:${RESET}${WHITE} " + nbOperation + EOL +
      s"${RESET}${YELLOW}Total number of operations:${RESET}${WHITE} "+ totNbOperationStringFormatted + EOL +
      s"${RESET}${YELLOW}Available CPU cores:${RESET}${WHITE} "+ typeOfDispatcher + EOL +
      s"${RESET}${BLUE}----------------------------------------${RESET}" + EOL + s"${RESET}${MAGENTA}${UNDERLINED}RUN PROGRESS${RESET}"

  }
}

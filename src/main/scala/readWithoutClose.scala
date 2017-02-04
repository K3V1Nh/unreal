import scala.io.Source

/**
  * Created by kevin on 9/12/16.
  */
class readWithoutClose(filename:String) extends readerFile(filename) {

  def read[A](value: String) = {
    if (value.equalsIgnoreCase("show"))
      readFileAndShowLines

    else if (value.equalsIgnoreCase("string"))
      readFileConvertToString

    else if (value.equalsIgnoreCase("list"))
      readFileConvertToList

    else
      logger.warn("Sorry but the value is not defined")
  }

  private def readFileAndShowLines{
    try {
      logger.info(s"Reading a file --> $filename ,encoding UTF-8")
      for (line <- Source.fromFile(filename,TYPE_ENCONDING).getLines) {
        println(line)
      }
    }catch{
      case e: Exception => logger.error(s"Probably there isn't exist the file $filename or exist another problem with the file")
    }finally
      logger.info(s"Closing the file $filename")
  }

  private def readFileConvertToString : String = {
    try {
      logger.info(s"Reading each line of file --> $filename and returning a single line, encoding UTF-8")
      return Source.fromFile(filename,TYPE_ENCONDING).getLines().mkString
    }catch{
      case e: Exception => logger.error(s"Probably there isn't exist the file $filename or exist another problem with the file")
        "" //return String empty
    }finally
      logger.info(s"Closing the file $filename")
  }

  private def readFileConvertToList : List [String] = {
    try{
      logger.info(s"Reading each line of file --> $filename and return a List, encoding UTF-8")
      return Source.fromFile(filename,TYPE_ENCONDING).getLines().toList
    }catch {
      case e: Exception => logger.error(s"Probably there isn't exist the file $filename or exist another problem with the file")
        Nil //return Array empty
    }finally
      logger.info(s"Closing the file $filename")
  }
}

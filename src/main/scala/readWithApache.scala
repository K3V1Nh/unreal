import java.io.File

import org.apache.commons.io.FileUtils

class readWithApache(filename:String) extends readerFile(filename){

  def read[A](value: String) = {
    if (value.equalsIgnoreCase("list"))
      apacheReadFile.toList
    else
      logger.warn("Sorry but the value is not defined")
  }

  private def apacheReadFile: Array[AnyRef] = {
    try {
      logger.info("Using library org.apache.commons.io.FileUtils, returning a Array[AnyRef], encoding UTF-8")
      val pathFile = new File(filename)
      val lines = FileUtils.readLines(pathFile, TYPE_ENCONDING)
      return lines.toArray
    } catch {
      case e: Exception => logger.error(s"Probably there isn't exist the file $filename or exist another problem with the file")
        Array[AnyRef]() // returning nothing in case of error
    } finally
      logger.info(s"Closing the file $filename")
  }

}

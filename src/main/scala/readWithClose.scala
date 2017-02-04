import scala.io.Source

class readWithClose(filename:String) extends readerFile(filename)  {

  def read[A](value: String) = {
    if (value.equalsIgnoreCase("list"))
        readFileConvertToListAndClose.toList.flatten
    else
      logger.warn("Sorry but the value is not defined")
  }

  //Closing the file open --> Best practice for manipulating files
  //Loan Pattern is with the using method described on the Loan Pattern website.
  private def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
  try {
    logger.info("Using simple method for close files in reads")
    f(resource)
  } finally {
    resource.close()
  }

  private def readFileConvertToListAndClose : Option[List[String]] ={
    try {
      logger.info(s"Reading file $filename returning a Option[List[String]], encoding UTF-8")
      logger.info(s"It can be necessary convert toList.flatten for obtain a simple list.")
      val lines = using(Source.fromFile(filename,TYPE_ENCONDING)) { source =>
        (for (line <- source.getLines) yield line).toList
      }
      Some(lines)
    } catch {
      case e: Exception => logger.error(s"Probably there isn't exist the file $filename or exist another problem with the file")
        None
    }finally
      logger.info(s"Closing the file $filename")
  }

}

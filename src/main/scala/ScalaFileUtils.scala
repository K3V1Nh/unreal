/**LOGGING**/
import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import java.io.{File,BufferedWriter,FileWriter,IOException,FileOutputStream,FileInputStream}
import org.slf4j.LoggerFactory
import org.apache.commons.io.FileUtils


object ScalaFileUtils {

  private val logger = LoggerFactory.getLogger("Utilidades")
  private val TYPE_ENCONDING = "UTF-8"

  /**
    * STRINGS
    */
  def isEqual( s1:String , s2:String ):Boolean = {
    logger.info("Comparing two String, ignoring uppercase and lowercase")
    s1.equalsIgnoreCase(s2)
  }

  /**
    * READ FILES
    * Closing the file opened --> Best practice for manipulating files
    */
  private def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
  try {
    logger.info("Using simple method for close files in reads")
    f(resource)
  } finally {
    resource.close()
  }

  def readFileConvertToListAndClose( filename: String ): Option[List[String]] ={
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

  /**
    * Making reads with org.apache.commons.io.FileUtils
    */
  def readFileWithApache( filename : String): Array[AnyRef] = {
    try {
      logger.info("Using library org.apache.commons.io.FileUtils, returning a Array[AnyRef], encoding UTF-8")
      val pathFile = new File(filename)
      val lines = FileUtils.readLines(pathFile, TYPE_ENCONDING)
      lines.toArray
    } catch {
      case e: Exception => logger.error(s"Probably there isn't exist the file $filename or exist another problem with the file")
        Array[AnyRef]() // returning nothing in case of error
    } finally
      logger.info(s"Closing the file $filename")
  }

  def readCSV (filename : String, delimiter:String, omitFirstLine : Boolean):ArrayBuffer[Array[String]] ={
    val omitLine = if (omitFirstLine) 1 else 0
    val rows = ArrayBuffer[Array[String]]()
    try {
      logger.info(s"Reading a CSV File $filename")
      using(Source.fromFile(filename,TYPE_ENCONDING)){ source =>
          for (line <- source.getLines.drop(omitLine)) {
            rows += line.split(",").map(_.trim)
          }
        }
      rows
    }catch {
      case e: Exception => logger.error(s"Probably there isn't exist the file $filename or exist another problem with the file")
        ArrayBuffer[Array[String]]() //
    }
  }

  /**
    *  WRITE FILES
    */
  def writeFile( filename: String , text : String): Boolean ={
    try {
      logger.info(s"Writing in File $filename")
      val file = new File(filename)
      val bw = new BufferedWriter(new FileWriter(file))
      bw.write(text)
      bw.close()
        true
    }catch {
      case e: IOException => e.printStackTrace()
      logger.error(s"Problem with the write, check the $filename")
        false
    }
  }

  /**
    * READ AND WRITE
    * Read each character --> byte to byte
    */
  def copyBinary( inputFile: String, outputFile: String): Boolean ={
    var in = None: Option[FileInputStream]
    var out = None: Option[FileOutputStream]
    try {
      logger.info(s"Open file $inputFile ")
      logger.info(s"Make a copy binary to $outputFile (foreach char).")
      in = Some(new FileInputStream(inputFile))
      out = Some(new FileOutputStream(outputFile))
      var c = 0
      while ({c = in.get.read; c != -1 }) {
        out.get.write(c)
      }
      true
    } catch {
      case e: IOException => e.printStackTrace()
      logger.error(s"Problem with the copy binary")
      false
    } finally {
      logger.info(s"Closing both files")
      if (in.isDefined) in.get.close()
      if (out.isDefined) out.get.close()
    }
  }

  /**
    * Count character foreach lines in a File
    */
  def countCharForEachLine(filename: String): Int = {
    try{
      logger.info(s"Open file $filename and count characters foreach line.")
      var newlineCount = 0
      using(Source.fromFile(filename,TYPE_ENCONDING)) { source =>
        for {
          line <- source.getLines()
          c <- line
        }newlineCount += 1
      }
      newlineCount
    }catch {
      case e: Exception => logger.error(s"Problem with the $filename ")
        0
    }
  }

  /**
    * Count lines in a File
    */
  def countLines(filename: String): Int = {
    try{
      logger.info(s"Open file $filename and count each line.")
      var newlineCount = 0
      using(Source.fromFile(filename,TYPE_ENCONDING)) { source =>
        for {
          line <- source.getLines()
        }newlineCount += 1
      }
      newlineCount
    }catch {
      case e: Exception => logger.error(s"Problem with the file $filename ")
        0
    }
  }

}
abstract class readerFile (val filename : String) extends ScalaFileUtils_master{

  val TYPE_ENCONDING = "UTF-8"
  def read [A](value: String):Any
  // method generic for read files and convert the result in any type

}

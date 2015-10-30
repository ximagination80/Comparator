import java.io.File

import org.scalatest.FunSuite

class EqualityTest extends FunSuite {

  val resDir = new File(System.getProperty("user.dir"), "/src/test/fs")
  require(resDir.exists())
  def file(name: String): File = new File(resDir, name)



}
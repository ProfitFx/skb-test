import org.scalatest.FreeSpec

/**
  * Created by Enot on 11.09.2016.
  */
class FreeSpe1c extends FreeSpec{
  "123" in {
    val dir=System.getProperty("testReportDir")
    println(dir)
    markup(dir)
  }
}

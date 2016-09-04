import java.io.FileInputStream

import com.typesafe.config.ConfigFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json4s.native.JsonMethods._
import org.scalatest.time.SpanSugar._
import org.scalatest.{DoNotDiscover, SequentialNestedSuiteExecution, Suites}

import scalaj.http.Http
import scalax.file.Path

/**
  * Created by smakhetov on 30.08.2016.
  */
@DoNotDiscover
class TestSuite extends Suites(
  new ПроверкаОтсутствияЗаявкиВВебИнтерфейсе,
  new ПовторнаяОтправкаСообщения,
  new ПроверкаФайлаЭксель,
  new ПроверкаЗаявкиВВебИнтерфейсе
)with SequentialNestedSuiteExecution

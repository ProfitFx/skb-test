import java.io.FileInputStream

import com.typesafe.config.ConfigFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.scalatest.{FreeSpec, Matchers}

import scalaj.http.Http
import org.json4s._
import org.json4s.native.JsonMethods._

import scalax.file.Path

/**
  * Created by Enot on 20.08.2016.
  *
  * работа с экселем
  * https://tproger.ru/translations/how-to-read-write-excel-file-java-poi-example/
  */
class GetTest extends FreeSpec{
  "Good test" in {
    assert(true)
  }
  "Bad test" in {
    assert(false)
  }
}


class ApiTest extends FreeSpec with Matchers {

  val conf = ConfigFactory.load


  "Отправка сообщения ORDERS через АПИ"  - {

    var token = ""
    var boxId = ""
    var authHeader = ""

    val url = conf.getString("api.url")
    val login = conf.getString("client.login")
    val password = conf.getString("client.password")
    var id = conf.getString("client.id")

    "Получение токена авторизации и формирование заголовка для последкющих вызовов API" in {
      val firstAuthHeader = s"KonturEdiAuth konturediauth_api_client_id=$id, konturediauth_login=$login, konturediauth_password=$password"
      val response = Http(s"$url/Authenticate").postForm.header("Authorization", firstAuthHeader).asString
      token = response.body
      authHeader = s"KonturEdiAuth konturediauth_api_client_id=$id, konturediauth_token=$token"
      println(token)
    }

    "Получение BoxesInfo и извлечение из него идентификатора ящика" in {
      val response = Http(s"$url/Boxes/GetBoxesInfo").header("Authorization", authHeader).asString
      println(response.body)
      val boxJson = parse(response.body)
      boxId = ((boxJson \ "Boxes").children(0) \"Id").values.toString
      println(boxId)
    }
//
//    "Получение событий ящиков" in {
//      val response = Http(s"$url/Boxes/GetBoxesInfo").header("Authorization", authHeader).asString
//      val boxJson = parse(response.body)
//      val boxes = (boxJson \ "Boxes").children
//      boxes.foreach(elem => {
//        val box = (elem \ "Id").values.toString
//        println(box)
//        val response = Http(s"$url/Messages/GetEvents?boxId=$box").header("Authorization", authHeader).asString
//        println("\n" + response.body)
//      })
//    }
//
//    "Отправка сообщения" ignore {
//
//      val postBody = scala.io.Source.fromFile("taskFiles/orders.txt").mkString
//      val response = Http(s"$url/Messages/SendMessage?boxId=$boxId").header("Authorization", authHeader).postData(postBody).asString
//      // val response = Http(s"$url/Messages/SendMessage?boxId=$boxId").postData("UNB+UNOE:3+1277777777773+1377777777770+20111204:1200+12345555'\nUNH+01+ORDERS:D:01B:UN:EAN010'\nBGM+220+NN01+9'\nDTM+137:201112041159:203'\nDTM+2:201112051200:203'\nRFF+CT:contractNumber'\nDTM+171:20131220:102'\nNAD+SU+1377777777770::9'\nRFF+YC1:21546'\nNAD+BY+1277777777773::9'\nNAD+DP+1277777777773::9'\nCUX+2:RUB:9'\nLIN+1++4600375914498:SRV'\nPIA+1+68778578:SA'\nPIA+1+358748:IN'\nIMD+F++:::GoodItem1'\nQTY+21:30.555:KGM'\nMOA+203:1527.75'\nPRI+AAA:50.0000'\nLIN+2++4366687650157:SRV'\nPIA+1+456:SA'\nPIA+1+358746:IN'\nIMD+F++:::GoodItem2'\nQTY+21:40:PCE'\nMOA+203:1865.58'\nPRI+AAA:46.6395'\nLIN+3++4600375914474:SRV'\nPIA+1+615464:SA'\nQTY+21:0:PCE'\nPRI+AAA:56.7800'\nLIN+4++4600375001112:SRV'\nPIA+1+111:SA'\nPIA+1+333:IN'\nIMD+F++:::GoodItme3'\nQTY+21:100:PCE'\nMOA+203:99995.00'\nPRI+AAA:999.95'\nUNS+S'\nMOA+125:106568.2760'\nCNT+2:7'\nUNT+48+01'\nUNZ+1+1'").header("Authorization", authHeader).param("boxId","e4e8b56d-3390-4e29-b7f5-169a83efacab").asString
//      println(response.body)
//      ///V1/Messages/GetOutboxMessage
//
//    }
//
//    "Получение тела сообщения" ignore {
//      val messageId = "9c2dccda-8899-45cf-bffc-80240bace7fb"
//      val response = Http(s"$url/Messages/GetInboxMessage?boxId=$boxId&messageId=$messageId").header("Authorization", authHeader).asString
//      println(response.body)
//    }
//
//    "Получение PartiesInfo" ignore {
//      val response = Http(s"$url/Parties/GetAccessiblePartiesInfo").header("Authorization", authHeader).asString
//      println(response.body)
//    }
//    "Получение GetEvents" in {
//      val response = Http(s"$url/Messages/GetEvents?boxId=$boxId").header("Authorization", authHeader).asString
//      println("\n" + response.body)
//    }

  }

  "Наличие ошибки при повторной отправке сообщения без изменений" ignore {
  }

  "В интерфейсе поставщика проверить наличие отправленного сообщения" ignore {
  }

  "В карточке сообщения на вкладке \"Заявка\" проверить" - {
    "Корректность названия товара №2 и №4 (названия можно найти и менять из файла ORDERS)" ignore {
    }

    "Скачать xlsx" ignore {

    }

  }
}

class ExcelTest extends FreeSpec with Matchers{

  "Проверить корректность названия товара №2 и №4 в скачанном файле" in {
    val fileName = "taskfiles/Черновик подтверждения заказа №NN01 от 04.12.2011.xlsx"

    val myExcelBook = new XSSFWorkbook(new FileInputStream(fileName))
    def cellValue(sheet: Int = 0, row: Int, col: Int): String = myExcelBook.getSheetAt(sheet).getRow(row).getCell(col).getStringCellValue

    val goodName1 = cellValue(row = 18, col = 2)
    goodName1 should be ("GoodItem1")
    System.out.println("name : " + goodName1)
    val goodName2 = cellValue(row = 21, col = 2)
    goodName2 should be ("GoodItem1")
    System.out.println("name : " + goodName2)
    myExcelBook.close()
  }
}

class WebTest extends FreeSpecWithBrowserScaledScreen{

  val conf = ConfigFactory.load
  val baseUrl = conf.getString("web.url")
  val login = conf.getString("client.login")
  val password = conf.getString("client.password")

  s"Авторизация на странице $baseUrl" in {
    go to baseUrl
    click on xpath("html/body/div[4]/div/div/div/div[2]/div/div[1]/table/tbody/tr/td[2]/a")
    emailField(xpath("html/body/div[4]/div/div/div/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div[2]/div/input")).value = login
    pwdField(xpath("html/body/div[4]/div/div/div/div[2]/div/div[2]/div/div[2]/div/div[2]/div/div[2]/div/input[1]")).value = password
    Thread.sleep(1000)
    click on xpath("html/body/div[4]/div/div/div/div[2]/div/div[2]/div/div[4]/div[2]/input")

  }
  "Загрузка файла" in {
    Path("downloads").deleteRecursively(true,true)
    click on xpath("html/body/div[1]/div/div[4]/div[4]/div[1]/div[1]/a/span")
    click on xpath("html/body/div[3]/div[1]/div[2]/div/div[2]/a/span[3]/span")
    Thread.sleep(5000)
  }
}

/**
  * Created by smakhetov on 17.05.2016.
  */

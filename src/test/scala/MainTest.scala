import java.io.FileInputStream

import com.typesafe.config.ConfigFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json4s.native.JsonMethods._
import org.scalatest.DoNotDiscover
import org.scalatest.time.SpanSugar._

import scalaj.http.Http
import scalax.file.Path

/**
  * Created by Enot on 04.09.2016.
  */
@DoNotDiscover
class MainTest extends FreeSpecWithBrowserScaledScreen {//with CancelAfterFailure{

  val conf = ConfigFactory.load
  val webUrl = conf.getString("web.url")
  val login = conf.getString("client.login")
  val password = conf.getString("client.password")
  // val orderNumber = conf.getString("testData.orderNumber")
  val orderNumber = "NN04"

  var boxId = conf.getString("api.boxId")
  var authHeader = ""
  var lastEventID = ""
  val apiUrl = conf.getString("api.url")
  var id = conf.getString("client.id")
  val fileName = s"downloads/Черновик подтверждения заказа №$orderNumber от 04.12.2011.xlsx"

  // Get запрос с авторизацией
  def getRequest(urlPath: String) = Http(apiUrl + urlPath).header("Authorization", authHeader).asString.body
  // Post запрос с авторизацией
  def postRequest(urlPath: String, postBody: String) = Http(apiUrl + urlPath).header("Authorization", authHeader).postData(postBody).asString.body


  "Проверка отсутствия записи в интерфейсе пользователя" - {

    s"Авторизация на странице $webUrl" in {
      go to webUrl
      //click on xpath("html/body/div[4]/div/div/div/div[2]/div/div[1]/table/tbody/tr/td[2]/a")
      click on xpath("//*[contains(text(), 'По паролю')]")
      emailField(xpath("html/body/div[4]/div/div/div/div[2]/div/div[2]/div/div[2]/div/div[1]/div/div[2]/div/input")).value = login
      pwdField(xpath("html/body/div[4]/div/div/div/div[2]/div/div[2]/div/div[2]/div/div[2]/div/div[2]/div/input[1]")).value = password
      click on xpath("html/body/div[4]/div/div/div/div[2]/div/div[2]/div/div[4]/div[2]/input")
    }

    """Проверка остутствия строки заявки на странице "Новые заявки"""" in {
      find(xpath(s"//*[contains(text(), '$orderNumber')]")) should be ('isEmpty)
      createScreenCaptureToReport()
      //find(xpath(s"//*[contains(text(), 'NN03')]")) should  be ('isEmpty)
    }

  }
  "Отправка нового сообщения" - {

    "Получение токена авторизации и формирование заголовка для последкющих вызовов API" in {
      val firstAuthHeader = s"KonturEdiAuth konturediauth_api_client_id=$id, konturediauth_login=$login, konturediauth_password=$password"
      val token = Http(s"$apiUrl/Authenticate").postForm.header("Authorization", firstAuthHeader).asString.body
      authHeader = s"KonturEdiAuth konturediauth_api_client_id=$id, konturediauth_token=$token"
    }

    "Получение идентификатора последнего события в ящике" in {
      //val response = Http(s"$apiUrl/Messages/GetEvents?boxId=$boxId").header("Authorization", authHeader).asString
      val response = getRequest(s"/Messages/GetEvents?boxId=$boxId&count=1000")
      val responseJson = parse(response)
      lastEventID = (responseJson \ "LastEventId").values.toString
      println(lastEventID)
    }

    "Отправка сообщения" in {
      val postBody = scala.io.Source.fromFile("testFiles/orders.txt").mkString.replaceFirst("NN00",orderNumber)
      //val response = Http(s"$apiUrl/Messages/SendMessage?boxId=$boxId").header("Authorization", authHeader).postData(postBody).asString
      val response = postRequest(s"/Messages/SendMessage?boxId=$boxId",postBody)
      //println(response)
      // Thread.sleep(20000)
    }

    "Получение событий ящика и проверка успешности доставки" in {
      eventually(timeout(20 seconds), interval(1000 millis)) {
        //val response = Http(s"$apiUrl/Messages/GetEvents?boxId=$boxId&exclusiveEventId=$lastEventID").header("Authorization", authHeader).asString
        val response = getRequest(s"/Messages/GetEvents?boxId=$boxId&exclusiveEventId=$lastEventID")
        val responseJson = parse(response)
        val events = (responseJson \ "Events").children
        events.length should be (4)
        (events(0) \ "EventType").values.toString should be("NewOutboxMessage")
        (events(1) \ "EventType").values.toString should be("RecognizeMessage")
        (events(2) \ "EventType").values.toString should be("MessageDelivered")
        (events(3) \ "EventType").values.toString should be("MessageReadByPartner")
        println(response)
      }
    }
  }

  "Проверка наличия записи в интерфейсе пользователя" in {
    eventually(timeout(30 seconds), interval(1000 millis)){
      reloadPage()
      //find(xpath(s"//*[contains(text(), '$orderNumber')]")) should  be ('isEmpty)
      find(xpath(s"//*[contains(text(), '$orderNumber')]")) should not be ('isEmpty)
      createScreenCaptureToReport()
    }
  }

  "Проверка содержимого Excel файла" - {

    "Загрузка файла" in {
      Path("downloads").deleteRecursively(true,true)
      clickOn(xpath(s"//*[contains(text(), '$orderNumber')]"))
      click on xpath("html/body/div[1]/div/div[4]/div[4]/div[3]/div[1]/div/div[1]/div/div[2]/span/a/span[2]")
      // Thread.sleep(3000)
      eventually{
        new java.io.File(fileName) should be ('exists)
      }
      createScreenCaptureToReport()
    }

    "Проверка значений в таблице Excel" in {

      val myExcelBook = new XSSFWorkbook(new FileInputStream(fileName))
      // Извлечение значения ячейки по координатам
      def cellValue(sheet: Int, row: Int, col: Int): String = myExcelBook.getSheetAt(sheet).getRow(row).getCell(col).getStringCellValue
      // Описание координат и проверяемых значения ячеек
      val excelCheckTable = Table (
        ("Лист", "Строка", "Столбец", "Ожидаемое значение"),
        (0,0,0, s"Подтверждение заказа №$orderNumber от 04.12.2011  (Черновик)"),
        (0,18,2, "GoodItem1"),
        (0,21,2, "GoodItme3")
      )

      forAll(excelCheckTable) {(sheet: Int,row: Int,col: Int,value: String) =>
        //s"""Значение ячейки на листе $sheet в строке $row в колонке $col должно быть "$value"""" in {
        cellValue(sheet,row,col) should be (value)
      }
      myExcelBook.close()
    }
  }

  "Проверка повторной отправки" - {

    "Получение идентификатора последнего события в ящике" in {
      //val response = Http(s"$apiUrl/Messages/GetEvents?boxId=$boxId").header("Authorization", authHeader).asString
      val response = getRequest(s"/Messages/GetEvents?boxId=$boxId&exclusiveEventId=$lastEventID")
      val responseJson = parse(response)
      lastEventID = (responseJson \ "LastEventId").values.toString
      println(lastEventID)
    }

    "Отправка сообщения повторно" in {
      val postBody = scala.io.Source.fromFile("testFiles/orders.txt").mkString.replaceFirst("NN00",orderNumber)
      //val response = Http(s"$apiUrl/Messages/SendMessage?boxId=$boxId").header("Authorization", authHeader).postData(postBody).asString
      val response = postRequest(s"/Messages/SendMessage?boxId=$boxId", postBody)
    }

    "Получение событий ящика и проверка дубликата" in {
      eventually(timeout(10 seconds), interval(1000 millis)) {
        //val response = Http(s"$apiUrl/Messages/GetEvents?boxId=$boxId&exclusiveEventId=$lastEventID").header("Authorization", authHeader).asString
        val response = getRequest(s"/Messages/GetEvents?boxId=$boxId&exclusiveEventId=$lastEventID")
        val responseJson = parse(response)
        val events = (responseJson \ "Events").children
        events.length should be (2)
        (events(0) \ "EventType").values.toString should be ("NewOutboxMessage")
        (events(1) \ "EventContent" \ "MessageUndeliveryReasons").children.head.values.toString should be ("Точно такой же файл уже был недавно обработан (дублирующая отправка?)")
        (events(1) \ "EventType").values.toString should be ("MessageUndelivered")

      }
    }
  }
}

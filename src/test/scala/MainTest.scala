import java.io.FileInputStream

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json4s.native.JsonMethods._
import org.scalatest.DoNotDiscover
import org.scalatest.time.SpanSugar._

import scalaj.http.Http

/**
  * Created by Enot on 04.09.2016.
  */
@DoNotDiscover
class MainTest extends FreeSpecWithBrowser {//with CancelAfterFailure{

  val webUrl = conf.getString("web.url")
  val login = conf.getString("client.login")
  val password = conf.getString("client.password")
  val orderNumber = conf.getString("testData.orderNumber")
  val boxId = conf.getString("api.boxId")
  val apiUrl = conf.getString("api.url")
  val id = conf.getString("client.id")
  val excelFileName = s"$downloadDir/Черновик подтверждения заказа №$orderNumber от 04.12.2011.xlsx"
  val postBody = scala.io.Source.fromFile("testFiles/orders.txt").mkString.replaceFirst("NN00",orderNumber)
  var authHeader = ""
  var lastEventID = ""
  var messageId = ""


  // Get запрос с авторизацией
  def getRequest(urlPath: String) = Http(apiUrl + urlPath).header("Authorization", authHeader).asString.body
  // Post запрос с авторизацией
  def postRequest(urlPath: String, postBody: String) = Http(apiUrl + urlPath).header("Authorization", authHeader).postData(postBody).asString.body



  "Проверка отсутствия записи в интерфейсе пользователя" - {

    s"Авторизация на странице $webUrl" in {
      go to webUrl
      click on linkText("По паролю")
      emailField(xpath("//input[@name='login']")).value = login
      pwdField(xpath("//input[@name='']")).value = password
      click on xpath("//div[2]/input")
    }

    """Проверка остутствия строки заявки на странице "Новые заявки"""" in {
      find(xpath(s"//*[contains(text(), '$orderNumber')]")) should be ('isEmpty)
      createScreenCaptureToReport()
    }
  }

  "Отправка нового сообщения c заказом" - {

    "Получение токена авторизации и формирование заголовка для последующих вызовов API" in {
      val firstAuthHeader = s"KonturEdiAuth konturediauth_api_client_id=$id, konturediauth_login=$login, konturediauth_password=$password"
      val token = Http(s"$apiUrl/Authenticate").postForm.header("Authorization", firstAuthHeader).asString.body
      authHeader = s"KonturEdiAuth konturediauth_api_client_id=$id, konturediauth_token=$token"
    }

    "Получение идентификатора последнего события в ящике" in {
      val response = getRequest(s"/Messages/GetEvents?boxId=$boxId&count=1000")
      val responseJson = parse(response)
      createJsonFileToReport(responseJson)
      lastEventID = (responseJson \ "LastEventId").values.toString

    }

    "Отправка сообщения" in {
      val response = postRequest(s"/Messages/SendMessage?boxId=$boxId",postBody)
      val responseJson = parse(response)
      createJsonFileToReport(responseJson)
      messageId = (responseJson \ "MessageId").values.toString
    }

    "Получение событий ящика и проверка успешности доставки" in {
      eventually(timeout(20 seconds), interval(1000 millis)) {
        val response = getRequest(s"/Messages/GetEvents?boxId=$boxId&exclusiveEventId=$lastEventID")
        val responseJson = parse(response)
        createJsonFileToReport(responseJson)
        val allEvents = (responseJson \ "Events").children
        val msgEvents = allEvents.filter(x => {(x \ "EventContent" \ "OutboxMessageMeta"\"MessageId").values == messageId})
        // Проверка событий в ответном сообщении
        msgEvents.length should be (4)
        (msgEvents(0) \ "EventType").values.toString should be("NewOutboxMessage")
        (msgEvents(1) \ "EventType").values.toString should be("RecognizeMessage")
        (msgEvents(2) \ "EventType").values.toString should be("MessageDelivered")
        (msgEvents(3) \ "EventType").values.toString should be("MessageReadByPartner")
        lastEventID = (responseJson \ "LastEventId").values.toString

      }
    }
  }

  "Проверка наличия записи в интерфейсе пользователя" in {
    eventually(timeout(20 seconds), interval(1000 millis)){
      reloadPage()
      find(xpath(s"//*[contains(text(), '$orderNumber')]")) should not be ('isEmpty)
      createScreenCaptureToReport()
    }
  }

  "Проверка содержимого Excel файла" - {

    "Загрузка файла" in {
      clickOn(xpath(s"//*[contains(text(), '$orderNumber')]"))
      click on xpath("//a[@id='ExcelPrintLink']/span[2]")
      eventually{
        new java.io.File(excelFileName) should be ('exists)
      }
      markup(s"""<a href='..\$excelFileName'>Файл Excel</a>""")
      createScreenCaptureToReport()
    }

    "Проверка значений в таблице Excel" in {
      val myExcelBook = new XSSFWorkbook(new FileInputStream(excelFileName))
      // Извлечение значения ячейки по координатам
      def cellValue(sheet: Int, row: Int, col: Int): String = myExcelBook.getSheetAt(sheet).getRow(row).getCell(col).getStringCellValue
      // Описание координат и проверяемых значения ячеек
      val excelCheckTable = Table (
        ("Лист", "Строка", "Столбец", "Ожидаемое значение"),
        (0,0,0, s"Подтверждение заказа №$orderNumber от 04.12.2011  (Черновик)"),
        (0,18,2, "GoodItem1"),
        (0,21,2, "GoodItme3")
      )
      // Для каждой строки таблицы проверяем соответствие ожидаемого и действительного значений
      forAll(excelCheckTable) {(sheet: Int,row: Int,col: Int,value: String) =>
        cellValue(sheet,row,col) should be (value)
      }
      myExcelBook.close()
    }
  }

  "Проверка повторной отправки" - {

    "Отправка сообщения повторно" in {
      val response = postRequest(s"/Messages/SendMessage?boxId=$boxId", postBody)
      val responseJson = parse(response)
      createJsonFileToReport(responseJson)
      messageId = (responseJson \ "MessageId").values.toString
    }

    "Получение событий ящика и проверка дубликата" in {
      eventually(timeout(20 seconds), interval(1000 millis)) {
        val response = getRequest(s"/Messages/GetEvents?boxId=$boxId&exclusiveEventId=$lastEventID")
        val responseJson = parse(response)
        createJsonFileToReport(responseJson)
        val allEvents = (responseJson \ "Events").children
        val msgEvents = allEvents.filter(x => {(x \ "EventContent" \ "OutboxMessageMeta"\"MessageId").values == messageId})
        msgEvents.length should be (2)
        (msgEvents(0) \ "EventType").values.toString should be ("NewOutboxMessage")
        (msgEvents(1) \ "EventContent" \ "MessageUndeliveryReasons").children.head.values.toString should be ("Точно такой же файл уже был недавно обработан (дублирующая отправка?)")
        (msgEvents(1) \ "EventType").values.toString should be ("MessageUndelivered")

      }
    }
  }
}

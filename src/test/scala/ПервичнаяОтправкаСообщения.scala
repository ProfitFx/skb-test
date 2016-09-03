import com.typesafe.config.ConfigFactory
import org.json4s.native.JsonMethods._
import org.scalatest.concurrent.Eventually
import org.scalatest.{FreeSpec, Matchers}

import scalaj.http.Http

/**
  * Created by smakhetov on 31.08.2016.
  */
class ПервичнаяОтправкаСообщения extends FreeSpec with Eventually with Matchers {

  val conf = ConfigFactory.load

  "Отправка сообщения ORDERS через АПИ" - {
    var token = ""
    var boxId = "e4e8b56d-3390-4e29-b7f5-169a83efacab"
    var authHeader = ""
    var lastEventID = ""

    val url = conf.getString("api.url")
    val login = conf.getString("client.login")
    val password = conf.getString("client.password")
    var id = conf.getString("client.id")
    val orderNumber = conf.getString("testData.orderNumber")

    "Получение токена авторизации и формирование заголовка для последкющих вызовов API" in {
      val firstAuthHeader = s"KonturEdiAuth konturediauth_api_client_id=$id, konturediauth_login=$login, konturediauth_password=$password"
      val response = Http(s"$url/Authenticate").postForm.header("Authorization", firstAuthHeader).asString
      token = response.body
      authHeader = s"KonturEdiAuth konturediauth_api_client_id=$id, konturediauth_token=$token"
      println(token)
    }

    "Получение идентификатора последнего события в ящике" in {
      val response = Http(s"$url/Messages/GetEvents?boxId=$boxId").header("Authorization", authHeader).asString
      val responseJson = parse(response.body)
      lastEventID = (responseJson \ "LastEventId").values.toString
      //  println(lastEventID)
    }

    "Отправка сообщения" in {
      val postBody = scala.io.Source.fromFile("testFiles/orders.txt").mkString.replaceFirst("NN00",orderNumber)
      val response = Http(s"$url/Messages/SendMessage?boxId=$boxId").header("Authorization", authHeader).postData(postBody).asString
      println(response.body)
      Thread.sleep(20000)
    }

    "Получение событий ящика и проверка дубликата" in {
      //   eventually(timeout(20 seconds), interval(1000 millis)) {
      val response = Http(s"$url/Messages/GetEvents?boxId=$boxId&exclusiveEventId=$lastEventID").header("Authorization", authHeader).asString
      //        val responseJson = parse(response.body)
      //        val events = (responseJson \ "Events").children
      //        events.length should equal(2)
      //        (events(1) \ "EventContent" \ "MessageUndeliveryReasons").children.head.values.toString should be("Точно такой же файл уже был недавно обработан (дублирующая отправка?)")
      //        (events(1) \ "EventType").values.toString should be("MessageUndelivered")
      println(response.body)
      // }
    }
  }
}

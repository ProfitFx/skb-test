import com.typesafe.config.ConfigFactory
import org.json4s.native.JsonMethods._
import org.scalatest.concurrent.Eventually
import org.scalatest.{CancelAfterFailure, FreeSpec, Matchers}
import org.scalatest.time.SpanSugar._

import scalaj.http.Http

/**
  * Created by Enot on 29.08.2016.
  */
class ApiOperations extends FreeSpec with Matchers with Eventually with CancelAfterFailure {

  val conf = ConfigFactory.load

  var token = ""
  var boxId = "e4e8b56d-3390-4e29-b7f5-169a83efacab"
  var authHeader = ""
  var lastEventID = ""

  val url = conf.getString("api.url")
  val login = conf.getString("client.login")
  val password = conf.getString("client.password")
  var id = conf.getString("client.id")

  val box1 = "bccbecfb-a370-4f17-8873-8e05aaeb0bdc"
  val box2 = "e4e8b56d-3390-4e29-b7f5-169a83efacab"
  val box3 = "89941797-22ad-4007-9257-f26395982c02"
  val box4 = "e4e8b56d-3390-4e29-b7f5-169a83efacab"


  "Получение токена авторизации и формирование заголовка для последкющих вызовов API" in {
    val firstAuthHeader = s"KonturEdiAuth konturediauth_api_client_id=$id, konturediauth_login=$login, konturediauth_password=$password"
    val response = Http(s"$url/Authenticate").postForm.header("Authorization", firstAuthHeader).asString
    token = response.body
    authHeader = s"KonturEdiAuth konturediauth_api_client_id=$id, konturediauth_token=$token"
    //println(token)
  }


  "Получение идентификатора последнего события в ящике" in {
    val response = Http(s"$url/Messages/GetEvents?boxId=$boxId").header("Authorization", authHeader).asString
    val responseJson = parse(response.body)
    lastEventID = (responseJson \ "LastEventId").values.toString
    //  println(lastEventID)
  }


  "Отправка сообщения 4" in {
    val postBody = scala.io.Source.fromFile("taskFiles/orders.txt").mkString
    val response = Http(s"$url/Messages/SendMessage?boxId=$boxId").header("Authorization", authHeader).postData(postBody).asString
    // println(response.body)
  }

  //  "Получение событий ящика" in {
  //      val response = Http(s"""$url/Messages/GetEvents?boxId=$boxId&exclusiveEventId=d2a0eb37-c667-4b7e-9353-aa29e97a1a0d""").header("Authorization", authHeader).asString
  ////      val responseJson = parse(response.body)
  ////      val eventID = (responseJson \ "LastEventId").values.toString
  ////      eventID should not be lastEventID
  //      println(response.body)
  //    }

  "Получение событий ящика" in {
    eventually(timeout(10 seconds), interval(500 millis)) {
      val response = Http(s"$url/Messages/GetEvents?boxId=$boxId&exclusiveEventId=$lastEventID").header("Authorization", authHeader).asString
      val responseJson = parse(response.body)
      val events = (responseJson \ "Events").children
      events.length should equal (2)
      (events(1) \ "EventContent" \ "MessageUndeliveryReasons").children.head.values.toString should be ("Точно такой же файл уже был недавно обработан (дублирующая отправка?)")
      (events(1) \ "EventType").values.toString should be ("MessageUndelivered")
      println(response.body)
    }
  }
}

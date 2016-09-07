/**
  * Created by Enot on 26.08.2016.
  */

import java.io.{PrintWriter, FileWriter, FileReader}
import java.util.Properties

import org.json4s._
import org.json4s.native.JsonMethods._

import scalax.file.Path


object mainApp extends App {
  val inJson = """{"Boxes":[{"Id":"bccbecfb-a370-4f17-8873-8e05aaeb0bdc","PartyId":"25a6dc25-4796-45b8-81bd-c43a7ccac371","Gln":"1377777777770","IsTest":true,"BoxSettings":{"DeliveryNotificationEnabled":false,"TransportType":"Api","IsMain":false,"DocumentTypes":["Any","Stsmsg"],"CustomMessageFormats":["Any"]}},{"Id":"e4e8b56d-3390-4e29-b7f5-169a83efacab","PartyId":"25a6dc25-4796-45b8-81bd-c43a7ccac371","Gln":"1377777777770","IsTest":false,"BoxSettings":{"DeliveryNotificationEnabled":false,"TransportType":"Api","IsMain":true,"DocumentTypes":["Any","Stsmsg"],"CustomMessageFormats":["Any"]}},{"Id":"89941797-22ad-4007-9257-f26395982c02","PartyId":"ce0e689e-9f1f-4e9a-a113-a68dda559832","Gln":"1277777777773","IsTest":true,"BoxSettings":{"DeliveryNotificationEnabled":false,"TransportType":"Api","IsMain":false,"DocumentTypes":["Any","Stsmsg"],"CustomMessageFormats":["Any"]}},{"Id":"cdea9719-54a4-4ff7-9fec-829b66765693","PartyId":"ce0e689e-9f1f-4e9a-a113-a68dda559832","Gln":"1277777777773","IsTest":false,"BoxSettings":{"DeliveryNotificationEnabled":false,"TransportType":"Api","IsMain":true,"DocumentTypes":["Any","Stsmsg"],"CustomMessageFormats":["Any"]}}]}"""
  val json = parse(inJson)
  println(json)
  //implicit val formats = DefaultFormats
  val id = ((json \ "Boxes").children(0) \"Id").values //children(0).values
  println(id)
  println(System.getProperty("user.dir"))
  Path("downloads").deleteRecursively(true,true)
}

object mainApp1 extends App {
  val inJson = "{\n\t\"Events\" : [{\n\t\t\t\"BoxId\" : \"e4e8b56d-3390-4e29-b7f5-169a83efacab\",\n\t\t\t\"PartyId\" : \"25a6dc25-4796-45b8-81bd-c43a7ccac371\",\n\t\t\t\"EventId\" : \"14f2fcf6-fc58-4552-96fe-396527b60823\",\n\t\t\t\"EventDateTime\" : \"2016-08-29T17:23:52.3571751Z\",\n\t\t\t\"EventType\" : \"NewOutboxMessage\",\n\t\t\t\"EventContent\" : {\n\t\t\t\t\"OutboxMessageMeta\" : {\n\t\t\t\t\t\"BoxId\" : \"e4e8b56d-3390-4e29-b7f5-169a83efacab\",\n\t\t\t\t\t\"MessageId\" : \"cc73de86-7411-47aa-940e-ca01c4ff9bc1\",\n\t\t\t\t\t\"DocumentCirculationId\" : \"ed022dbf-ca8f-4278-8439-d6518f34d31e\"\n\t\t\t\t}\n\t\t\t}\n\t\t}, {\n\t\t\t\"BoxId\" : \"e4e8b56d-3390-4e29-b7f5-169a83efacab\",\n\t\t\t\"PartyId\" : \"25a6dc25-4796-45b8-81bd-c43a7ccac371\",\n\t\t\t\"EventId\" : \"eba45512-42bc-4d7e-a176-425d3d3fb4d9\",\n\t\t\t\"EventDateTime\" : \"2016-08-29T17:23:52.6158276Z\",\n\t\t\t\"EventType\" : \"MessageUndelivered\",\n\t\t\t\"EventContent\" : {\n\t\t\t\t\"MessageUndeliveryReasons\" : [\"Точно такой же файл уже был недавно обработан (дублирующая отправка?)\"],\n\t\t\t\t\"OutboxMessageMeta\" : {\n\t\t\t\t\t\"BoxId\" : \"e4e8b56d-3390-4e29-b7f5-169a83efacab\",\n\t\t\t\t\t\"MessageId\" : \"cc73de86-7411-47aa-940e-ca01c4ff9bc1\",\n\t\t\t\t\t\"DocumentCirculationId\" : \"ed022dbf-ca8f-4278-8439-d6518f34d31e\"\n\t\t\t\t}\n\t\t\t}\n\t\t}, {\n\t\t\t\"BoxId\" : \"e4e8b56d-3390-4e29-b7f5-169a83efacab\",\n\t\t\t\"PartyId\" : \"25a6dc25-4796-45b8-81bd-c43a7ccac371\",\n\t\t\t\"EventId\" : \"326aa038-6555-4656-9c89-6990b4bec211\",\n\t\t\t\"EventDateTime\" : \"2016-08-29T17:37:05.912506Z\",\n\t\t\t\"EventType\" : \"NewOutboxMessage\",\n\t\t\t\"EventContent\" : {\n\t\t\t\t\"OutboxMessageMeta\" : {\n\t\t\t\t\t\"BoxId\" : \"e4e8b56d-3390-4e29-b7f5-169a83efacab\",\n\t\t\t\t\t\"MessageId\" : \"ceec310f-348c-4f6d-b096-0e847f828539\",\n\t\t\t\t\t\"DocumentCirculationId\" : \"c9b0e67b-7a75-409f-97f1-870bc5ebec3f\"\n\t\t\t\t}\n\t\t\t}\n\t\t}, {\n\t\t\t\"BoxId\" : \"e4e8b56d-3390-4e29-b7f5-169a83efacab\",\n\t\t\t\"PartyId\" : \"25a6dc25-4796-45b8-81bd-c43a7ccac371\",\n\t\t\t\"EventId\" : \"f3ccbc65-20ae-4fef-8027-caad429e6381\",\n\t\t\t\"EventDateTime\" : \"2016-08-29T17:37:06.1303699Z\",\n\t\t\t\"EventType\" : \"MessageUndelivered\",\n\t\t\t\"EventContent\" : {\n\t\t\t\t\"MessageUndeliveryReasons\" : [\"Точно такой же файл уже был недавно обработан (дублирующая отправка?)\"],\n\t\t\t\t\"OutboxMessageMeta\" : {\n\t\t\t\t\t\"BoxId\" : \"e4e8b56d-3390-4e29-b7f5-169a83efacab\",\n\t\t\t\t\t\"MessageId\" : \"ceec310f-348c-4f6d-b096-0e847f828539\",\n\t\t\t\t\t\"DocumentCirculationId\" : \"c9b0e67b-7a75-409f-97f1-870bc5ebec3f\"\n\t\t\t\t}\n\t\t\t}\n\t\t}\n\t],\n\t\"LastEventId\" : \"f3ccbc65-20ae-4fef-8027-caad429e6381\"\n}"
  val responseJson = parse(inJson)
  val events = (responseJson \ "Events").children
  println(events.length)
  println((events(1) \ "EventContent" \ "MessageUndeliveryReasons").children.head.values.toString)
  println((events(1) \ "EventType").values.toString)
  val json = pretty(render(responseJson))
  println(json)
  new PrintWriter("filename.txt") { write(json); close }
}

object mainApp2 extends App {
  val patt = "123".r
  val s = "01234512345"
  val m = patt.findAllMatchIn(s).toList
  m.foreach(a => println(a))
}

object mainApp3 extends App {
  val resp = """{"Events":[{"BoxId":"e4e8b56d-3390-4e29-b7f5-169a83efacab","PartyId":"25a6dc25-4796-45b8-81bd-c43a7ccac371","EventId":"36ceb7d4-eda0-4177-89fc-73a9bb959ba1","EventDateTime":"2016-09-05T19:53:01.9846399Z","EventType":"NewOutboxMessage","EventContent":{"OutboxMessageMeta":{"BoxId":"e4e8b56d-3390-4e29-b7f5-169a83efacab","MessageId":"a4eb5ddb-2f6b-4806-a610-574eada37bc0","DocumentCirculationId":"8ab76f19-19ca-4420-a133-e60a65c54c57"}}},{"BoxId":"e4e8b56d-3390-4e29-b7f5-169a83efacab","PartyId":"25a6dc25-4796-45b8-81bd-c43a7ccac371","EventId":"45c7948d-2da5-4b1e-858b-15eb726d2791","EventDateTime":"2016-09-05T19:53:02.2301358Z","EventType":"MessageUndelivered","EventContent":{"MessageUndeliveryReasons":["Точно такой же файл уже был недавно обработан (дублирующая отправка?)"],"OutboxMessageMeta":{"BoxId":"e4e8b56d-3390-4e29-b7f5-169a83efacab","MessageId":"a4eb5ddb-2f6b-4806-a610-574eada37bc0","DocumentCirculationId":"8ab76f19-19ca-4420-a133-e60a65c54c57"}}}],"LastEventId":"45c7948d-2da5-4b1e-858b-15eb726d2791"}"""
  val responseJson = parse(resp)
  val events = (responseJson \ "Events").children
  println(events.length)
  println((events(0) \ "EventType").values.toString)
  println((events(1) \ "EventContent" \ "MessageUndeliveryReasons").children.head.values.toString)
  //    println((events(1) \ "EventType").values.toString)
}

object mainApp4 extends App {
  val props = new Properties
  val reader = new FileReader("1.properties")
  props.load(reader)
  var value = props.getProperty("key","defValue")
  value = "100500"
  props.setProperty("key", value)
  val writer = new FileWriter("1.properties")
  props.store(writer, "vsyo")
}

object mainApp5 extends App {
  new PrintWriter("filename.txt") { write("file contents"); close }
  //reflect.io.File("report/response.json").writeAll("response")
}
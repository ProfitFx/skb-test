/**
  * Created by Enot on 26.08.2016.
  */

import org.json4s._
import org.json4s.native.JsonMethods._

object mainApp extends App {

  val inJson = """{"Boxes":[{"Id":"bccbecfb-a370-4f17-8873-8e05aaeb0bdc","PartyId":"25a6dc25-4796-45b8-81bd-c43a7ccac371","Gln":"1377777777770","IsTest":true,"BoxSettings":{"DeliveryNotificationEnabled":false,"TransportType":"Api","IsMain":false,"DocumentTypes":["Any","Stsmsg"],"CustomMessageFormats":["Any"]}},{"Id":"e4e8b56d-3390-4e29-b7f5-169a83efacab","PartyId":"25a6dc25-4796-45b8-81bd-c43a7ccac371","Gln":"1377777777770","IsTest":false,"BoxSettings":{"DeliveryNotificationEnabled":false,"TransportType":"Api","IsMain":true,"DocumentTypes":["Any","Stsmsg"],"CustomMessageFormats":["Any"]}},{"Id":"89941797-22ad-4007-9257-f26395982c02","PartyId":"ce0e689e-9f1f-4e9a-a113-a68dda559832","Gln":"1277777777773","IsTest":true,"BoxSettings":{"DeliveryNotificationEnabled":false,"TransportType":"Api","IsMain":false,"DocumentTypes":["Any","Stsmsg"],"CustomMessageFormats":["Any"]}},{"Id":"cdea9719-54a4-4ff7-9fec-829b66765693","PartyId":"ce0e689e-9f1f-4e9a-a113-a68dda559832","Gln":"1277777777773","IsTest":false,"BoxSettings":{"DeliveryNotificationEnabled":false,"TransportType":"Api","IsMain":true,"DocumentTypes":["Any","Stsmsg"],"CustomMessageFormats":["Any"]}}]}"""
  val json = parse(inJson)
  println(json)
  //implicit val formats = DefaultFormats
  val id = ((json \ "Boxes").children(0) \ "Id").values //children(0).values
  println(id)
}

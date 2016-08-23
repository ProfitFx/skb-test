import org.scalatest.{FreeSpec, Matchers}

import scalaj.http.{Http, HttpResponse}

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


class MainTest extends FreeSpec with Matchers {
  /** Что надо протестировать:
    * # Отправку сообщения ORDERS через АПИ.
    * # Наличие ошибки при повторной отправке сообщения без изменений.
    * # В интерфейсе поставщика проверить наличие отправленного сообщения.
    * # В карточке сообщения на вкладке "Заявка" проверить:
    * ## Корректность названия товара №2 и №4 (названия можно найти и менять из файла ORDERS).
    * ## Скачать xlsx и проверить корректность названия товара №2 и №4 в скачанном файле.
    */



  "Отправка сообщения ORDERS через АПИ"  - {

    var token = ""

    "Получение токена авторизации "in {
      val authHeader = "KonturEdiAuth konturediauth_api_client_id=profitfx-e1d4b253-79d2-41c2-9304-be97d6bb48cd , konturediauth_login=profitfx@mail.ru, konturediauth_password=Ko624205!"
      val response = Http("https://test-edi-api.kontur.ru/V1/Authenticate").postForm.header("Authorization", authHeader).asString
      token = response.body
      println(token)
    }

    "Получение BoxesInfo" in {
      val authHeader = s"KonturEdiAuth konturediauth_api_client_id=profitfx-e1d4b253-79d2-41c2-9304-be97d6bb48cd , konturediauth_token=$token"
      val response = Http("https://test-edi-api.kontur.ru/V1/Boxes/GetBoxesInfo").header("Authorization", authHeader).asString
      println(response.body)
    }

    "Отправка сообщения" in {
      val authHeader = s"KonturEdiAuth konturediauth_api_client_id=profitfx-e1d4b253-79d2-41c2-9304-be97d6bb48cd , konturediauth_token=$token"
      val response = Http("https://test-edi-api.kontur.ru/V1/Messages/SendMessage?boxId=e4e8b56d-3390-4e29-b7f5-169a83efacab").postData("UNB+UNOE:3+1277777777773+1377777777770+20111204:1200+12345555'\nUNH+01+ORDERS:D:01B:UN:EAN010'\nBGM+220+NN01+9'\nDTM+137:201112041159:203'\nDTM+2:201112051200:203'\nRFF+CT:contractNumber'\nDTM+171:20131220:102'\nNAD+SU+1377777777770::9'\nRFF+YC1:21546'\nNAD+BY+1277777777773::9'\nNAD+DP+1277777777773::9'\nCUX+2:RUB:9'\nLIN+1++4600375914498:SRV'\nPIA+1+68778578:SA'\nPIA+1+358748:IN'\nIMD+F++:::GoodItem1'\nQTY+21:30.555:KGM'\nMOA+203:1527.75'\nPRI+AAA:50.0000'\nLIN+2++4366687650157:SRV'\nPIA+1+456:SA'\nPIA+1+358746:IN'\nIMD+F++:::GoodItem2'\nQTY+21:40:PCE'\nMOA+203:1865.58'\nPRI+AAA:46.6395'\nLIN+3++4600375914474:SRV'\nPIA+1+615464:SA'\nQTY+21:0:PCE'\nPRI+AAA:56.7800'\nLIN+4++4600375001112:SRV'\nPIA+1+111:SA'\nPIA+1+333:IN'\nIMD+F++:::GoodItme3'\nQTY+21:100:PCE'\nMOA+203:99995.00'\nPRI+AAA:999.95'\nUNS+S'\nMOA+125:106568.2760'\nCNT+2:7'\nUNT+48+01'\nUNZ+1+1'").header("Authorization", authHeader).param("boxId","e4e8b56d-3390-4e29-b7f5-169a83efacab").asString
      println(response.body)
    }

  }

  "Наличие ошибки при повторной отправке сообщения без изменений" in {

  }

  "В интерфейсе поставщика проверить наличие отправленного сообщения" in {

  }

  "В карточке сообщения на вкладке \"Заявка\" проверить" - {
    "Корректность названия товара №2 и №4 (названия можно найти и менять из файла ORDERS)" in {

    }

    "Скачать xlsx и проверить корректность названия товара №2 и №4 в скачанном файле" in {

    }

  }
}

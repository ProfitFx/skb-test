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

  "Отправка сообщения ORDERS через АПИ" in {
    val response: HttpResponse[String] = Http("https://test-edi-api.kontur.ru/V1/Boxes/GetBoxesInfo").header("Authorization","profitfx-e1d4b253-79d2-41c2-9304-be97d6bb48cd").asString
    println(response.body)
    println(response.code)
    println(response.headers)
    println(response.cookies)
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

import org.scalatest.{DoNotDiscover, FreeSpec}

/**
  * Created by Enot on 11.09.2016.
  */
@DoNotDiscover
class FreeSpecTst extends FreeSpec{
  "123" in {
    val dir=System.getProperty("testReportDir")
    println(dir)
    markup(dir)
  }
}

@DoNotDiscover
class createRep extends FreeSpecWithBrowser{

  val webUrl = conf.getString("web.url")
  val login = conf.getString("client.login")
  val password = conf.getString("client.password")
  val orderNumber = conf.getString("testData.orderNumber")
  val excelFileName = s"Черновик подтверждения заказа №$orderNumber от 04.12.2011.xlsx"

  s"Авторизация на странице $webUrl" in {
    go to webUrl
    click on linkText("По паролю")
    emailField(xpath("//input[@name='login']")).value = login
    pwdField(xpath("//input[@name='']")).value = password
    click on xpath("//div[2]/input")
  }

  "Загрузка файла" in {
    eventually {
      click on xpath(s"//*[contains(text(), '$orderNumber')]")
    }
    eventually {
      click on xpath("//a[@id='ExcelPrintLink']/span[2]")
    }
    eventually{
      new java.io.File(s"$reportDir/$excelFileName") should be ('exists)
    }
    markup(s"""<a href='$excelFileName'>Файл Excel</a>""")
    createScreenCaptureToReport()
  }
}
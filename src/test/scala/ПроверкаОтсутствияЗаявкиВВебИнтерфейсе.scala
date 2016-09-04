import com.typesafe.config.ConfigFactory
import org.scalatest.DoNotDiscover

/**
  * Created by smakhetov on 04.09.2016.
  */


@DoNotDiscover
class ПроверкаОтсутствияЗаявкиВВебИнтерфейсе extends FreeSpecWithBrowserScaledScreen{

  // val baseUrl = "https://habrahabr.ru/company/cit/blog/269293/"
  //  "Open" in {
  //    go to baseUrl
  //    //"html/body/div[1]/div[2]/div[3]/div[2]/div[2]/div/div[2]/div[5]"
  //  //  println(find(xpath("html/body/div[1]/div[2]/div[3]/div[2]/div[2]/div/div[2]")).get.text)
  //    val el = find(xpath("html/body/div[1]/div[2]/div[3]/div[2]/div[2]/div/div[2]"))
  //      el.foreach(ele => { ele.text should not include("преобразование")})
  //  }



  // .//*[@id='ScrollFix']/div[4]/div[4]/div[4]/div[2]/a
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
    //eventually{println(find(xpath("html/body/div[1]/div/div[4]/div[4]/div[4]/div[2]/a/span[4]/span[2]/span")).get.text)}
  }

  "Проверка остутствия строки заявки" in {
    println(find(xpath("html/body/div[1]/div/div[4]/div[4]/div[4]/div[2]")).get.text)
    println(find(xpath("//*[contains(text(), 'NN00')]")).isEmpty)
    find(xpath("//*[contains(text(), 'NN00')]")) should  be ('isEmpty)
    find(xpath("//*[contains(text(), 'NN02')]")) should not be ('isEmpty)
    println(find(xpath("//*[contains(text(), 'NN02')]")).isEmpty)
    //click on (xpath("//*[contains(text(), 'NN02')]"))
    // Thread.sleep(5000)
  }
  //
  //  "Загрузка файла" in {
  //    Path("downloads").deleteRecursively(true,true)
  //    click on xpath("html/body/div[1]/div/div[4]/div[4]/div[4]/div[2]/a/span[4]/span[2]/span")
  //                   "html/body/div[1]/div/div[4]/div[4]/div[4]/div[2]"
  //    click on xpath("html/body/div[1]/div/div[4]/div[4]/div[3]/div[1]/div/div[1]/div/div[2]/span/a/span[2]")
  //    Thread.sleep(2000)
  //  }

}
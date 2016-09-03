import com.typesafe.config.ConfigFactory
import org.scalatest.DoNotDiscover

import scalax.file.Path

/**
  * Created by smakhetov on 31.08.2016.
  */

@DoNotDiscover
class ПроверкаЗаявкиВВебИнтерфейсе extends FreeSpecWithBrowserScaledScreen{

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


  "Загрузка файла" in {
    Path("downloads").deleteRecursively(true,true)
    click on xpath("html/body/div[1]/div/div[4]/div[4]/div[4]/div[2]/a/span[4]/span[2]/span")
    click on xpath("html/body/div[1]/div/div[4]/div[4]/div[3]/div[1]/div/div[1]/div/div[2]/span/a/span[2]")
    Thread.sleep(2000)
  }

}

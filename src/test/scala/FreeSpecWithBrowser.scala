import java.io.PrintWriter
import java.util

import com.typesafe.config.ConfigFactory
import org.json4s
import org.json4s.native.JsonMethods._
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxProfile}
import org.openqa.selenium.remote.{CapabilityType, DesiredCapabilities}
import org.scalatest._
import org.scalatest.concurrent.{Eventually, TimeLimits}
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.selenium.WebBrowser
import org.scalatest.time.SpanSugar._

import scalax.file.Path

/**
  * Created by smakhetov on 11.04.2016.
  */
trait FreeSpecWithBrowser extends FreeSpec with Matchers with WebBrowser with Eventually with TimeLimits with BeforeAndAfter with BeforeAndAfterAll with TableDrivenPropertyChecks{

  val conf = ConfigFactory.load
  val reportDir=System.getProperty("testReportDir")
  val eventuallyTimeout = conf.getInt("eventually.timeout") // Секунд
  val eventuallyInterval = conf.getInt("eventually.interval") // Милисекунд
  val browserType = conf.getInt("browser")
  val browserDownloadDir = (System.getProperty("user.dir") + "/" + reportDir).replace("/","\\")
  //println(browserDownloadDir)
  // Настройки для блока "eventually", который пытается отловить элемент в течение "timeout" с интервалом "interval"
  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = eventuallyTimeout seconds, interval = eventuallyInterval millis)
  // Указание папки для скриншотов
  setCaptureDir(reportDir)

  // Настройка профиля для Firefox
  val firefoxProfile = new FirefoxProfile()
  firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream")
  firefoxProfile.setPreference("browser.download.folderList",2)
  firefoxProfile.setPreference("browser.download.manager.showWhenStarting",false)
  firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false)
  firefoxProfile.setPreference("browser.download.dir", browserDownloadDir)

  // Настройка профиля для Chrome
  val chromePrefs = new util.Hashtable[String, Any]
  chromePrefs.put("profile.default_content_settings.popups", 0)
  chromePrefs.put("download.default_directory", browserDownloadDir)
  val options = new ChromeOptions
  options.setExperimentalOption("prefs", chromePrefs)
  val chromeCapabilities = DesiredCapabilities.chrome
  chromeCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true)
  chromeCapabilities.setCapability(ChromeOptions.CAPABILITY, options)

  // Определение браузера
    implicit val webDriver: WebDriver =  browserType match {
      case 1 => new FirefoxDriver(firefoxProfile)
      case 2 => new ChromeDriver(chromeCapabilities)
    }

  //implicit var webDriver: WebDriver = new FirefoxDriver(firefoxProfile)
  //implicit var webDriver: WebDriver = new ChromeDriver(chromeCapabilities)

  webDriver.manage().window().maximize()

  // Код выполняется до шагов
  override def beforeAll = {
  }

  // Код выполняется после всех шагов
  override def afterAll{
    quit
  }

  // Метод создает скрин с именем TimeStamp и вставляет его в отчет
  def createScreenCaptureToReport(fileName: String = System.currentTimeMillis + ".png", scale: Int = 25): Unit = {
    captureTo(fileName)
    markup(s"<img src='$fileName' width='$scale%'/>")
  }

  // Метод создает файл с отформатированным json файлом и вставляет его в отчет
  def createJsonFileToReport(fileContent: json4s.JValue, description: String = "Ответное json сообщение", fileName: String = System.currentTimeMillis + ".json"): Unit = {
    val json = pretty(render(fileContent))
    new PrintWriter(s"$reportDir/$fileName") { write(json); close }
    markup(s"""<a href='$fileName'>$description</a>""")
  }

  // При падении делаем скриншот окна браузера
  override def withFixture(test: NoArgTest) = {
    super.withFixture(test) match {
      case failed: Failed =>
        createScreenCaptureToReport(scale = 100)
        failed
      case other =>
        other
    }
  }
}

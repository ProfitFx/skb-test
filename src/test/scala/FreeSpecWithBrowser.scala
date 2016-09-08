import java.io.PrintWriter
import com.typesafe.config.ConfigFactory
import org.json4s
import org.json4s.native.JsonMethods._
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxProfile}
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
  val reportDir = conf.getString("testDir.report")
  val downloadDir = conf.getString("testDir.downloads")
  val eventuallyTimeout = conf.getInt("eventually.timeout") // Секунд
  val eventuallyInterval = conf.getInt("eventually.interval") // Милисекунд

  // Настройки для блока "eventually", который пытается отловить элемент в течение "timeout" с интервалом "interval"
  implicit override val patienceConfig = PatienceConfig(timeout = eventuallyTimeout seconds, interval = eventuallyInterval millis)
  // Указание папки для скриншотов
  setCaptureDir(reportDir)

  // Настройка профиля для Firefox
  val firefoxProfile = new FirefoxProfile()
  firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream")
  firefoxProfile.setPreference("browser.download.folderList",2)
  firefoxProfile.setPreference("browser.download.manager.showWhenStarting",false)
  firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false)
  firefoxProfile.setPreference("browser.download.dir", System.getProperty("user.dir") + s"/$downloadDir/")

  implicit var webDriver: WebDriver = new FirefoxDriver(firefoxProfile)
  webDriver.manage().window().maximize()

  // Код выполняется до шагов
  override def beforeAll = {
    Path(downloadDir).deleteRecursively(true,true)
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

  // При падении делаем скриншот
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

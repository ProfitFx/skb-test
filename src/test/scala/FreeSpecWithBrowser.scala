import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxProfile}
import org.scalatest._
import org.scalatest.concurrent.{Eventually, Timeouts}
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.selenium.WebBrowser
import org.scalatest.time.{Millis, Seconds, Span}

/**
  * Created by smakhetov on 11.04.2016.
  */
trait FreeSpecWithBrowser extends FreeSpec with Matchers with WebBrowser with Eventually with Timeouts with BeforeAndAfter with BeforeAndAfterAll with TableDrivenPropertyChecks{
  //Настройки для блока "eventually", который пытается отловить элемент в течение "timeout" с интервалом "interval"
  //Без этого блока тест упадет просто не найдя элемента на странице
  val eventuallyTimeout = 3 // Секунд
  val eventuallyInterval = 250 // Милисекунд
  val browserType = 2 // 1 - Firefox, 2 - Chrome, 3 - IE


  implicit override val patienceConfig = PatienceConfig(timeout = scaled(Span(eventuallyTimeout, Seconds)), interval = scaled(Span(eventuallyInterval, Millis)))
  //implicit override val patienceConfig = PatienceConfig(timeout = eventuallyTimeout millis, interval = eventuallyInterval millis)
  setCaptureDir("report")

  val firefoxProfile = new FirefoxProfile()

  // firefoxProfile.setPreference("browser.download.folderList",2)
  // firefoxProfile.setPreference("browser.download.manager.showWhenStarting",false)
  // firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
  // firefoxProfile.setPreference("browser.download.dir", System.getProperty("user.home") + "/Downloads/");


  firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", mimeTypes.s)

  WebDriver driver = new FirefoxDriver(firefoxProfile);

  //  implicit val webDriver: WebDriver =  browserType match {
  //    case 1 => new FirefoxDriver(firefoxProfile)
  //    case 2 => new ChromeDriver()
  //    case 3 => new InternetExplorerDriver()
  //    //case _ => "any"
  //  }
  webDriver.manage().window().maximize()


  override def afterAll(){//Код выполняется после всех шагов
    quit()
  }

  val baseURL = "https://mpvk-test.pub.centre-it.com/MPVK/"

}

trait FreeSpecWithBrowserScaledScreen extends FreeSpecWithBrowser{

  def createScreenCaptureToReport(fileName: String = System.currentTimeMillis + ".png", scale: Int = 25): Unit = {// Метод создает скрин с именем TimeStamp и вставляет его в отчет
    captureTo(fileName)
    markup(s"<img src='${fileName}' width='${scale}%' />")
  }

  override def withFixture(test: NoArgTest) = { // при падении делаем скриншот
    super.withFixture(test) match {
      case failed: Failed =>
        markup(s"""<a href ="${currentUrl}">${currentUrl}</a>""")
        createScreenCaptureToReport(scale = 100)
        click on xpath(".//*[@id='vw-navbar']/div/div/ul[1]/li[1]/a")
        failed
      case other =>
        markup(s"""<a href ="${currentUrl}">${currentUrl}</a>""")
        createScreenCaptureToReport()
        other
    }
  }
}







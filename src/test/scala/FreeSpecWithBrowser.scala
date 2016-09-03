import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxProfile}
import org.scalatest._
import org.scalatest.concurrent.{Eventually, TimeLimits}
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.selenium.WebBrowser
import org.scalatest.time.SpanSugar._

/**
  * Created by smakhetov on 11.04.2016.
  */
trait FreeSpecWithBrowser extends FreeSpec with Matchers with WebBrowser with Eventually with TimeLimits with BeforeAndAfter with BeforeAndAfterAll with TableDrivenPropertyChecks{
  //Настройки для блока "eventually", который пытается отловить элемент в течение "timeout" с интервалом "interval"
  //Без этого блока тест упадет просто не найдя элемента на странице



  val eventuallyTimeout = 3 // Секунд
  val eventuallyInterval = 250 // Милисекунд

  //implicit override val patienceConfig = PatienceConfig(timeout = scaled(Span(eventuallyTimeout, Seconds)), interval = scaled(Span(eventuallyInterval, Millis)))
  implicit override val patienceConfig = PatienceConfig(timeout = eventuallyTimeout millis, interval = eventuallyInterval millis)
  setCaptureDir("report")
  val firefoxProfile = new FirefoxProfile()
  firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream")
  firefoxProfile.setPreference("browser.download.folderList",2)
  firefoxProfile.setPreference("browser.download.manager.showWhenStarting",false)
  firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false)
  //firefoxProfile.setPreference("browser.download.dir", System.getProperty("user.home") + "/Downloads/")
  firefoxProfile.setPreference("browser.download.dir", System.getProperty("user.dir") + """\downloads\""")
  implicit var webDriver: WebDriver = new FirefoxDriver(firefoxProfile)


override def beforeAll = {
  //webDriver = new FirefoxDriver(firefoxProfile)
  webDriver.manage().window().maximize()
}


  override def afterAll{//Код выполняется после всех шагов
    quit()
  }

}

trait FreeSpecWithBrowserScaledScreen extends FreeSpecWithBrowser{

  def createScreenCaptureToReport(fileName: String = System.currentTimeMillis + ".png", scale: Int = 25): Unit = {// Метод создает скрин с именем TimeStamp и вставляет его в отчет
    captureTo(fileName)
    markup(s"<img src='$fileName' width='$scale%' />")
  }

  override def withFixture(test: NoArgTest) = { // при падении делаем скриншот
    super.withFixture(test) match {
      case failed: Failed =>
        markup(s"""<a href ="$currentUrl">$currentUrl</a>""")
        createScreenCaptureToReport(scale = 100)
        click on xpath(".//*[@id='vw-navbar']/div/div/ul[1]/li[1]/a")
        failed
      case other =>
        markup(s"""<a href ="$currentUrl">$currentUrl</a>""")
        createScreenCaptureToReport()
        other
    }
  }
}

//  implicit val webDriver: WebDriver =  browserType match {
//    case 1 => new FirefoxDriver(firefoxProfile)
//    case 2 => new ChromeDriver()
//    case 3 => new InternetExplorerDriver()
//    //case _ => "any"
//  }


// firefoxProfile.setPreference("browser.download.folderList",2)
// firefoxProfile.setPreference("browser.download.manager.showWhenStarting",false)
// firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
// firefoxProfile.setPreference("browser.download.dir", System.getProperty("user.home") + "/Downloads/");

object mimeTypes {
  //val s2 = "application/x-sh;image/prs.btif;application/vnd.businessobjects;application/x-bzip;application/x-bzip2;application/x-csh"
 // val s3 = "text/plain-bas;application/x-bcpio;application/octet-stream;image/bmp;application/x-bittorrent;application/vnd.rim.cod;application/vnd.blueice.multipass;application/vnd.bm"
 // val s3 = "application/vnd.rim.cod;application/vnd.blueice.multipass;application/vnd.bm"
  val s4 = "application/octet-stream"
  val s5 = "text/plain-bas;application/x-bcpio;application/x-bittorrent;image/bmp"
}


import org.scalatest.{DoNotDiscover, SequentialNestedSuiteExecution, Suites}

/**
  * Created by smakhetov on 30.08.2016.
  */
@DoNotDiscover
class TestSuite extends Suites(new ReportTest)with SequentialNestedSuiteExecution

// new ПроверкаОтсутствияЗаявкиВВебИнтерфейсе,
// new ПовторнаяОтправкаСообщения,
// new ПроверкаФайлаЭксель,
// new ПроверкаЗаявкиВВебИнтерфейсе
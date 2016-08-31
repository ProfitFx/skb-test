import org.scalatest.{SequentialNestedSuiteExecution, Suites}

/**
  * Created by smakhetov on 30.08.2016.
  */
class TestSuite extends Suites(
  new ПовторнаяОтправкаСообщения,
  new ПроверкаФайлаЭксель
)with SequentialNestedSuiteExecution

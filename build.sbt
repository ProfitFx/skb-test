name := "skb-test"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test->*",
  "org.apache.poi" % "poi-ooxml-schemas" % "3.14",
  "org.apache.poi" % "poi-ooxml" % "3.14",
  "org.apache.poi" % "poi" % "3.14",
  "org.scalaj" % "scalaj-http_2.11" % "2.3.0",
  "org.json4s" % "json4s-native_2.11" % "3.4.0",
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.5",
  "org.seleniumhq.selenium" % "selenium-java" % "2.53.1",
  "com.typesafe" % "config" % "1.3.0",
  "com.github.scala-incubator.io" % "scala-io-file_2.11" % "0.4.3-1"
)

//Опции для создания отчета в формате html "-h" в папке "report" и текстового файла "-f" "report.txt" без поддержки цвета "W"(потому как в винде выглядит коряво)
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-hD", "report", "-fW", "report.txt")

//http://ftp.mozilla.org/pub/firefox/releases/47.0.1/win32/ru/

//parallelExecution in Test := false
//libraryDependencies += "org.pegdown" % "pegdown" % "1.6.0"
//assemblyOutputPath in assembly := baseDirectory.value / "tests.jar"

cleanFiles <+= baseDirectory { base => base / "report" }
cleanFiles <+= baseDirectory { base => base / "report.txt" }
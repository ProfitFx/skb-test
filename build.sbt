name := "skb-test"

version := "1.0"

scalaVersion := "2.11.8"
// https://mvnrepository.com/artifact/org.scalatest/scalatest_2.11
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test->*"
// https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml-schemas
libraryDependencies += "org.apache.poi" % "poi-ooxml-schemas" % "3.14"
// https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml
libraryDependencies += "org.apache.poi" % "poi-ooxml" % "3.14"
// https://mvnrepository.com/artifact/org.apache.poi/poi
libraryDependencies += "org.apache.poi" % "poi" % "3.14"
// https://mvnrepository.com/artifact/org.scalaj/scalaj-http_2.11
libraryDependencies += "org.scalaj" % "scalaj-http_2.11" % "2.3.0"
// https://mvnrepository.com/artifact/org.json4s/json4s-native_2.11
libraryDependencies += "org.json4s" % "json4s-native_2.11" % "3.4.0"

libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.5"


libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.53.1"

//libraryDependencies += "org.pegdown" % "pegdown" % "1.6.0"

//Опции для создания отчета в формате html "-h" в папке "report" и текстового файла "-f" "report.txt" без поддержки цвета "W"(потому как в винде выглядит коряво)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-hD", "report", "-fW", "report.txt")

parallelExecution in Test := false

assemblyOutputPath in assembly := baseDirectory.value / "tests.jar"
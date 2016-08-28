name := "skb-test"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test->*"

libraryDependencies += "org.apache.poi" % "poi-ooxml-schemas" % "3.14"

libraryDependencies += "org.apache.poi" % "poi-ooxml" % "3.14"

libraryDependencies += "org.apache.poi" % "poi" % "3.14"

libraryDependencies += "org.scalaj" % "scalaj-http_2.11" % "2.3.0"

libraryDependencies += "org.json4s" % "json4s-native_2.11" % "3.4.0"

libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.5"

libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.53.1"

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

libraryDependencies += "com.github.scala-incubator.io" % "scala-io-file_2.11" % "0.4.3-1"

//libraryDependencies += "org.pegdown" % "pegdown" % "1.6.0"

//Опции для создания отчета в формате html "-h" в папке "report" и текстового файла "-f" "report.txt" без поддержки цвета "W"(потому как в винде выглядит коряво)

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-hD", "report", "-fW", "report.txt")

parallelExecution in Test := false

assemblyOutputPath in assembly := baseDirectory.value / "tests.jar"


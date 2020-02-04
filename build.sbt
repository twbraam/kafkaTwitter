name := "kafkaTwitter"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= List(
  "org.apache.kafka" % "kafka-clients" % "2.2.1",
  "com.twitter" % "hbc-core" % "2.2.0",
  "com.google.code.gson" % "gson" % "2.8.6"
)

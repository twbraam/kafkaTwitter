name := "kafkaTwitter"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies ++= List(
  "org.apache.kafka" % "kafka-clients" % "2.2.1",
  "com.twitter" % "hbc-core" % "2.2.0",
  "com.google.code.gson" % "gson" % "2.8.6",
  "org.apache.flink" %% "flink-scala" % "1.9.2",
  "org.apache.flink" %% "flink-streaming-scala" % "1.9.2"
)

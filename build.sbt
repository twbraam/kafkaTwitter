name := "kafkaTwitter"

version := "0.1"

scalaVersion := "2.12.10"

val kafkaVersion = "2.2.1"
val flinkVersion = "1.10.0"

libraryDependencies ++= List(
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  "org.apache.kafka" % "kafka-streams" % kafkaVersion,
  "com.twitter" % "hbc-core" % "2.2.0",
  "com.google.code.gson" % "gson" % "2.8.6",
  "org.apache.flink" %% "flink-scala" % flinkVersion,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
  "org.apache.flink" %% "flink-connector-kafka" % flinkVersion,
  "org.apache.commons" % "commons-text" % "1.8",
  "org.twitter4j" % "twitter4j-core" % "4.0.7"


)

package com.twbraam.twittercurator.config

object KafkaConfiguration {
  val SERVERS = "localhost:9094"
  val TOPIC_TWITTER = "bigdata-tweets"
  val TOPIC_TIMED = "bigdata-tweets-timed"
}

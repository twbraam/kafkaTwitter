package com.twbraam.kafkatwitter

import com.twbraam.kafkatwitter.producer.TwitterKafkaProducer

object Main extends App {
  println("Hello World!")
  val producer = new TwitterKafkaProducer
  producer.run()
}

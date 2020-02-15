package com.twbraam.twittercurator.twittertimed.producer

import java.time.Duration
import java.util
import java.util.{Properties, UUID}

import com.google.gson.Gson
import com.twbraam.twittercurator.callback.BasicCallback
import com.twbraam.twittercurator.config.KafkaConfiguration
import com.twbraam.twittercurator.twittertimed.model.Tweet
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.{LongDeserializer, LongSerializer, StringDeserializer, StringSerializer}

import scala.collection.JavaConverters._


object TwitterTimedProducer {
  val gson = new Gson

  def getProducer: KafkaProducer[Long, String] = {
    val props = new Properties
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfiguration.SERVERS)
    props.setProperty(ProducerConfig.ACKS_CONFIG, "1")
    props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "500")
    props.setProperty(ProducerConfig.RETRIES_CONFIG, "0")
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[LongSerializer].getName)
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    new KafkaProducer[Long, String](props)
  }

  def getConsumer: KafkaConsumer[Long, String] = {
    val props = new Properties
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfiguration.SERVERS)
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID.toString)
    props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")
    props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[LongDeserializer].getName)
    props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    new KafkaConsumer[Long, String](props)
  }

  @scala.annotation.tailrec
  def checkIfDayOld(tweetTimestamp: Long): Unit = {
    val age = System.currentTimeMillis - tweetTimestamp

    if (age < 86400000) {
      val toWait = 86400000 - age
      println(s"Tweet is too young ($age), trying again in ${toWait / 1000} seconds (or ${toWait / 1000 / 60} minutes or ${toWait / 1000 / 60 / 60} hours)")
      Thread.sleep(toWait + 1)
      checkIfDayOld(tweetTimestamp)
    } else println("Tweet is at least a day old")
  }

  def run(): Unit = {
    val consumer = getConsumer
    val topics: util.List[String] = List(KafkaConfiguration.TOPIC_TWITTER).asJava

    consumer.subscribe(topics)

    val producer = getProducer

    while (true) {
      val records = consumer.poll(Duration.ofMillis(100))
      for (record <- records.asScala) {
        val (offset, key, value) = (record.offset, record.key, record.value)
        println(s"Tweet: $value")
        val tweet: Tweet = gson.fromJson(value, classOf[Tweet])
        println("text: " + tweet.text)
        println("ts: " + tweet.timestamp)
        println("id: " + tweet.id)
        println("user: " + tweet.user)


        val tweetTimestamp: Long = tweet.timestamp
        checkIfDayOld(tweetTimestamp)

        val newRecord = new ProducerRecord[Long, String](KafkaConfiguration.TOPIC_TIMED, key, value)

        producer.send(newRecord, BasicCallback)
      }
    }
  }
}
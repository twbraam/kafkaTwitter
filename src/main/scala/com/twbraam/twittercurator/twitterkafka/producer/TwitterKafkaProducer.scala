package com.twbraam.twittercurator.twitterkafka.producer

import java.util.concurrent.LinkedBlockingQueue
import java.util.{Collections, Properties}

import com.google.gson.Gson
import com.twbraam.twittercurator.utils.config.{KafkaConfiguration, TwitterConfiguration}
import com.twbraam.twittercurator.utils.model.StaleTweet
import com.twbraam.twittercurator.utils.twitter.TwitterStreamClient
import com.twbraam.twittercurator.utils.twitter.callback.BasicCallback
import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.BasicClient
import com.twitter.hbc.httpclient.auth.OAuth1
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.{LongSerializer, StringSerializer}
import org.apache.commons.text.StringEscapeUtils.escapeJava


object TwitterKafkaProducer {
  val gson: Gson = new Gson
  val twitter: TwitterStreamClient = TwitterStreamClient(TwitterConfiguration.HASHTAG)

  val producer: KafkaProducer[Long, String] = {
    val props = new Properties
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfiguration.SERVERS)
    props.setProperty(ProducerConfig.ACKS_CONFIG, "1")
    props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "500")
    props.setProperty(ProducerConfig.RETRIES_CONFIG, "0")
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[LongSerializer].getName)
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    new KafkaProducer[Long, String](props)
  }

  def run(): Unit = {
    println("Started TwitterKafka...")

    twitter.client.connect()

    try while (true) {
      val tweetString = twitter.queue.take
      val tweet = gson.fromJson(tweetString, classOf[StaleTweet])

      val key = tweet.id
      val value = tweet.toString
      val record = new ProducerRecord[Long, String](KafkaConfiguration.TOPIC_TWITTER, key, value)

      producer.send(record, BasicCallback)
    } catch {
      case e: InterruptedException => e.printStackTrace()
    } finally {
      twitter.client.stop()
      if (producer != null) producer.close()
    }
  }
}
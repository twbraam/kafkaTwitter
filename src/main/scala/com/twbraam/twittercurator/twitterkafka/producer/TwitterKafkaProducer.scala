package com.twbraam.twittercurator.twitterkafka.producer

import java.util.concurrent.LinkedBlockingQueue
import java.util.{Collections, Properties}

import com.google.gson.Gson
import com.twbraam.twittercurator.twitterkafka.config.{KafkaConfiguration, TwitterConfiguration}
import com.twbraam.twittercurator.twitterkafka.model.Tweet
import com.twbraam.twittercurator.twitterkafka.producer.callback.BasicCallback
import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.BasicClient
import com.twitter.hbc.httpclient.auth.OAuth1
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.{LongSerializer, StringSerializer}

object TwitterKafkaProducer {

  // Configure auth
  val authentication = new OAuth1(
    TwitterConfiguration.CONSUMER_KEY,
    TwitterConfiguration.CONSUMER_SECRET,
    TwitterConfiguration.ACCESS_TOKEN,
    TwitterConfiguration.TOKEN_SECRET
  )

  // track the terms of your choice. here im only tracking #bigdata.
  val endpoint = new StatusesFilterEndpoint
  endpoint.trackTerms(Collections.singletonList(TwitterConfiguration.HASHTAG))

  val queue = new LinkedBlockingQueue[String](10000)
  val client: BasicClient = new ClientBuilder()
    .hosts(Constants.STREAM_HOST)
    .authentication(authentication)
    .endpoint(endpoint)
    .processor(new StringDelimitedProcessor(queue)).build
  val gson = new Gson
  val callback = new BasicCallback

  def getProducer: KafkaProducer[Long, String] = {
    val properties = new Properties
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfiguration.SERVERS)
    properties.put(ProducerConfig.ACKS_CONFIG, "1")
    properties.put(ProducerConfig.LINGER_MS_CONFIG, 500)
    properties.put(ProducerConfig.RETRIES_CONFIG, 0)
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[LongSerializer].getName)
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    new KafkaProducer[Long, String](properties)
  }

  def run(): Unit = {
    client.connect()
    val producer = getProducer
    try while (true) {
      val tweet = gson.fromJson(queue.take, classOf[Tweet])
      println(s"Fetched tweet id ${tweet.id}")

      val key = tweet.id
      val msg = tweet.toString
      val record = new ProducerRecord[Long, String](KafkaConfiguration.TOPIC, key, msg)

      producer.send(record, callback)
    } catch {
      case e: InterruptedException => e.printStackTrace()
    } finally {
      client.stop()
      if (producer != null) producer.close()
    }
  }
}
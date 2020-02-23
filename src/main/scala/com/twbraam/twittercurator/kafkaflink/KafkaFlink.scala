package com.twbraam.twittercurator.kafkaflink

import java.util.{Properties, UUID}

import com.twbraam.twittercurator.utils.config.KafkaConfiguration
import com.twbraam.twittercurator.kafkaflink.processor.{TweetParser, TweetRefresher}
import com.twbraam.twittercurator.kafkaflink.sink.TweetSink
import com.twbraam.twittercurator.utils.model.{FreshTweet, StaleTweet}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig

object KafkaFlink extends App {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  val props = new Properties()
  props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfiguration.SERVERS)
  props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID.toString)

  val source: DataStream[String] = env
    .addSource(new FlinkKafkaConsumer[String](KafkaConfiguration.TOPIC_TIMED, new SimpleStringSchema, props).setStartFromEarliest())
    .name("source")

  val staleTweets: DataStream[StaleTweet] = source
    .process(new TweetParser)
    .name("tweets")

  val freshTweets: DataStream[FreshTweet] = staleTweets
    .process(new TweetRefresher)
    .name("tweets")


  freshTweets
    .addSink(new TweetSink)
    .name("print-tweets")

  env.execute("Tweet Interpreter")


}

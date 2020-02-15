package com.twbraam.twittercurator.kafkaflink

import java.util.{Properties, UUID}

import com.twbraam.twittercurator.config.KafkaConfiguration
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.flink.streaming.api.scala._

object KafkaFlink extends App {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment



  val props = new Properties()
  props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfiguration.SERVERS)
  props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID.toString)

  val stream: DataStreamSink[String] = env
    .addSource(new FlinkKafkaConsumer[String](KafkaConfiguration.TOPIC_TIMED, new SimpleStringSchema(), props).setStartFromEarliest())
    .print()

  env.execute("Test")


}

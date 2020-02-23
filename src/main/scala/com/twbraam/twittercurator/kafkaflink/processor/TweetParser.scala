package com.twbraam.twittercurator.kafkaflink.processor

import com.twbraam.twittercurator.kafkatimer.producer.KafkaTimerProducer.gson
import com.twbraam.twittercurator.utils.model.StaleTweet
import org.apache.flink.streaming.api.functions.{KeyedProcessFunction, ProcessFunction}
import org.apache.flink.util.Collector


object TweetParser {

}

@SerialVersionUID(1L)
class TweetParser extends ProcessFunction[String, StaleTweet] {

  @throws[Exception]
  def processElement(
                      element: String,
                      context: ProcessFunction[String, StaleTweet]#Context,
                      collector: Collector[StaleTweet]): Unit = {

    val tweet = gson.fromJson(element, classOf[StaleTweet])

    collector.collect(tweet)
  }
}


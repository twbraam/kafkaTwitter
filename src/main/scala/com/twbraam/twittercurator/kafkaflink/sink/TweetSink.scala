package com.twbraam.twittercurator.kafkaflink.sink

import com.twbraam.twittercurator.utils.model.FreshTweet
import org.apache.flink.annotation.PublicEvolving
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * A sink for outputting alerts.
 */

object TweetSink {

  private val LOG: Logger = LoggerFactory.getLogger(classOf[TweetSink])

}

@PublicEvolving
@SuppressWarnings(Array("unused"))
@SerialVersionUID(1L)
class TweetSink extends SinkFunction[FreshTweet] {

  override def invoke(value: FreshTweet, context: SinkFunction.Context[_]): Unit = {

    TweetSink.LOG.info(value.toString)

  }

}
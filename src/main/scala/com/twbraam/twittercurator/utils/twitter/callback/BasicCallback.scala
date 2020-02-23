package com.twbraam.twittercurator.utils.twitter.callback

import org.apache.kafka.clients.producer.{Callback, RecordMetadata}

object BasicCallback extends Callback {
  override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
    if (exception == null)
      println(s"Message with offset ${metadata.offset} acknowledged by partition ${metadata.partition}")
    else
      println(exception.getMessage)
  }
}
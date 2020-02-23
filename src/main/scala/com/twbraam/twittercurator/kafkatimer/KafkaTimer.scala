package com.twbraam.twittercurator.kafkatimer

import com.twbraam.twittercurator.kafkatimer.producer.KafkaTimerProducer

object KafkaTimer extends App {
  KafkaTimerProducer.run()
}

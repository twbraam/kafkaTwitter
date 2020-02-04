package com.twbraam.kafkatwitter.model

import com.google.gson.annotations.SerializedName
import com.twbraam.kafkatwitter.model.Tweet.SerializedNameField
import scala.annotation.meta.field


case class Tweet(id: Long,
                 text: String,
                 user: User,
                 @SerializedNameField(value = "timestamp_ms") timestamp: Int) {

  override def toString: String =
    "Tweet{" +
      s"id=$id, " +
      s"text='$text', " +
      s"user=$user, " +
      "}"
}

object Tweet {
  type SerializedNameField = SerializedName @field
}
package com.twbraam.twittercurator.twitterkafka.model

import com.google.gson.annotations.SerializedName
import Tweet.SerializedNameField
import scala.annotation.meta.field


case class Tweet(id: Long,
                 text: String,
                 user: User,
                 @SerializedNameField(value = "timestamp_ms") timestamp: Long) {

  override def toString: String =
    "Tweet{" +
      s"id=$id, " +
      s"text='$text', " +
      s"user=$user, " +
      s"timestamp=$timestamp" +
      "}"
}

object Tweet {
  type SerializedNameField = SerializedName @field
}
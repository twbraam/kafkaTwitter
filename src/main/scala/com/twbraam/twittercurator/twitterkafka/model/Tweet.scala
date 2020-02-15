package com.twbraam.twittercurator.twitterkafka.model

import com.google.gson.annotations.SerializedName
import Tweet.SerializedNameField
import org.apache.commons.text.StringEscapeUtils.escapeJava

import scala.annotation.meta.field


case class Tweet(id: Long,
                 text: String,
                 user: User,
                 @(SerializedName @scala.annotation.meta.field)("timestamp_ms") timestamp: Long) {

  override def toString: String =
    s"""
       |{
       |"id": $id,
       |"text": "${escapeJava(text)}",
       |"user": $user,
       |"timestamp": $timestamp
       |}
      """.stripMargin
}

object Tweet {
  type SerializedNameField = SerializedName @field
}
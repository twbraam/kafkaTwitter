package com.twbraam.twittercurator.twittertimed.model

import com.twbraam.twittercurator.twitterkafka.model.User
import org.apache.commons.text.StringEscapeUtils.escapeJava


case class Tweet(id: Long,
                 text: String,
                 user: User,
                 timestamp: Long) {

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
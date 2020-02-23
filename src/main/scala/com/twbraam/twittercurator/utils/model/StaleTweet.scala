package com.twbraam.twittercurator.utils.model

import org.apache.commons.text.StringEscapeUtils.escapeJava


case class StaleTweet(id: Long,
                      text: String,
                      user: User,
                      timestamp_ms: Long) {

  override def toString: String =
    s"""
       |{
       |"id": $id,
       |"text": "${escapeJava(text)}",
       |"user": $user,
       |"timestamp_ms": $timestamp_ms
       |}
      """.stripMargin
}
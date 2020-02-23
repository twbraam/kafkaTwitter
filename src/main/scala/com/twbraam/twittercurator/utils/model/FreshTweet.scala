package com.twbraam.twittercurator.utils.model

import org.apache.commons.text.StringEscapeUtils.escapeJava


case class FreshTweet(id: Long,
                      text: String,
                      user: User,
                      retweets: Long,
                      timestamp: Long) {

  override def toString: String =
    s"""
       |{
       |"id": $id,
       |"text": "${escapeJava(text)}",
       |"user": $user,
       |"retweets": $retweets
       |}
      """.stripMargin
}
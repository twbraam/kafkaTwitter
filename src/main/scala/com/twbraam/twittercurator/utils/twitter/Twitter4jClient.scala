package com.twbraam.twittercurator.utils.twitter

import com.twbraam.twittercurator.utils.config.TwitterConfiguration
import com.twbraam.twittercurator.utils.model.{FreshTweet, User}
import twitter4j.{Status, Twitter, TwitterFactory}
import twitter4j.conf.ConfigurationBuilder

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

class Twitter4jClient(twitter: Twitter) {

  def refreshTweets(tweetIds: ListBuffer[Long]): List[FreshTweet] = {
    val statuses = twitter.tweets().lookup(tweetIds: _*)

    val it: Iterator[Status] = statuses.iterator.asScala

    val freshTweets = it.map { status =>
      val twitter4jUser: twitter4j.User = status.getUser
      val user = User(twitter4jUser.getId, twitter4jUser.getName)

      FreshTweet(status.getId, status.getText, user, status.getRetweetCount.toLong, status.getCreatedAt.getTime)
    }

    freshTweets.toList
  }

}

object Twitter4jClient {

  def apply(): Twitter4jClient = {

    val cb = new ConfigurationBuilder

    cb.setDebugEnabled(true)
      .setOAuthConsumerKey(TwitterConfiguration.CONSUMER_KEY)
      .setOAuthConsumerSecret(TwitterConfiguration.CONSUMER_SECRET)
      .setOAuthAccessToken(TwitterConfiguration.ACCESS_TOKEN)
      .setOAuthAccessTokenSecret(TwitterConfiguration.TOKEN_SECRET)
    val tf = new TwitterFactory(cb.build)

    new Twitter4jClient(tf.getInstance)
  }

}

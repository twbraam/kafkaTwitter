package com.twbraam.twittercurator.utils.twitter

import java.util

import com.twbraam.twittercurator.utils.config.TwitterConfiguration
import twitter4j.{Status, Twitter, TwitterFactory}
import twitter4j.conf.ConfigurationBuilder
import scala.collection.JavaConverters._

object Twitter4jClient {

  def main(args : Array[String]) {

    // (1) config work to create a twitter object
    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey(TwitterConfiguration.CONSUMER_KEY)
      .setOAuthConsumerSecret(TwitterConfiguration.CONSUMER_SECRET)
      .setOAuthAccessToken(TwitterConfiguration.ACCESS_TOKEN)
      .setOAuthAccessTokenSecret(TwitterConfiguration.TOKEN_SECRET)
    val tf = new TwitterFactory(cb.build)
    val twitter = tf.getInstance

    // (2) use the twitter object to get your friend's timeline
    val list = List(1231564490501689351L, 1231462445706645504L)
    val statuses = twitter.tweets().lookup(list: _*)
    val it = statuses.iterator.asScala
    it.foreach(status =>
      println(status.getFavoriteCount + " " + status.getRetweetCount))
  }

}

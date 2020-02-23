package com.twbraam.twittercurator.utils.twitter

import java.util.Collections
import java.util.concurrent.LinkedBlockingQueue

import com.google.gson.Gson
import com.twbraam.twittercurator.utils.config.TwitterConfiguration
import com.twbraam.twittercurator.utils.twitter.TwitterStreamClient.authentication
import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import com.twitter.hbc.httpclient.BasicClient
import com.twitter.hbc.httpclient.auth.OAuth1

case class TwitterStreamClient(hashtag: String) {
  val endpoint = new StatusesFilterEndpoint
  endpoint.trackTerms(Collections.singletonList(hashtag))

  val queue = new LinkedBlockingQueue[String](10000)

  val client: BasicClient = new ClientBuilder()
    .hosts(Constants.STREAM_HOST)
    .authentication(authentication)
    .endpoint(endpoint)
    .processor(new StringDelimitedProcessor(queue)).build
}

object TwitterStreamClient {
  val authentication = new OAuth1(
    TwitterConfiguration.CONSUMER_KEY,
    TwitterConfiguration.CONSUMER_SECRET,
    TwitterConfiguration.ACCESS_TOKEN,
    TwitterConfiguration.TOKEN_SECRET
  )
}

package com.twbraam.kafkatwitter.model

import com.google.gson.annotations.SerializedName
import com.twbraam.kafkatwitter.model.User.SerializedNameField
import scala.annotation.meta.field


case class User(id: Long,
                name: String,
                @SerializedNameField(value = "screen_name") screenName: String,
                location: String,
                @SerializedNameField(value = "followers_count") followersCount: Int) {

  override def toString: String =
    "User{" +
      s"id=$id, " +
      s"name='$name', " +
      s"screenName='$screenName', " +
      s"location='$location', " +
      s"followersCount=$followersCount" +
      "}"
}

object User {
  type SerializedNameField = SerializedName @field
}
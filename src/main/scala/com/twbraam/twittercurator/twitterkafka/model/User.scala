package com.twbraam.twittercurator.twitterkafka.model

import com.google.gson.annotations.SerializedName

import scala.annotation.meta.field


case class User(id: Long,
                name: String) {

  override def toString: String =
    "User{" +
      s"id=$id, " +
      s"name='$name'" +
      "}"
}

object User {
  type SerializedNameField = SerializedName @field
}
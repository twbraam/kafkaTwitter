package com.twbraam.twittercurator.utils.model

import com.google.gson.annotations.SerializedName
import org.apache.commons.text.StringEscapeUtils.escapeJava

import scala.annotation.meta.field


case class User(id: Long,
                name: String) {

  override def toString: String =
    s"""
       |{
       |"id": $id,
       |"name": "${escapeJava(name)}"
       |}
      """.stripMargin.replaceAll("\n", " ")
}

object User {
  type SerializedNameField = SerializedName @field
}
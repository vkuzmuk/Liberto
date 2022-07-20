package com.vlkuzmuk.freedomcry.models

data class EventModel(
    var post_text: String = "",
    var location: String = "",
    var anonimity: String = "false",
    var username: String = "",
    var photoUrl: String = "empty",
    var time: String = "0"
)
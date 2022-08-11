package com.vlkuzmuk.freedomcry.models

data class EventModel(
    var post_text: String = "",
    var location: String = "",
    var anonimity: String = "false",
    var username: String = "",
    var photoUrl: String = "empty",
    var time: String = "0",
    var key: String? = null,
    var uid: String = "unknown",
    var hasReaction: Boolean = false,
    var isPlanned: Boolean = false,
    var reactionCounter: String = "0"
)

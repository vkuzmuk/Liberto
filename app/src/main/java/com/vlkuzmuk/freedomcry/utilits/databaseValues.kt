package com.vlkuzmuk.freedomcry.utilits

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.vlkuzmuk.freedomcry.database.DbManager
import com.vlkuzmuk.freedomcry.models.UserModel

lateinit var AUTH: FirebaseAuth
lateinit var USER: UserModel
lateinit var CURRENT_UID: String

lateinit var REF_DATABASE_ROOT: DatabaseReference
//nodes
const val NODE_USERNAMES = "usernames"
const val NODE_USERS = "users"
const val NODE_EVENTS = "events"
const val NODE_POST_IMAGES = "post_images"

// children
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_EVENT = "event"


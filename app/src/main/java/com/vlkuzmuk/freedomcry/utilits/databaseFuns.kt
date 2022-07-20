package com.vlkuzmuk.freedomcry.utilits

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.vlkuzmuk.freedomcry.database.DbManager
import com.vlkuzmuk.freedomcry.models.UserModel

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    val database = Firebase.database
    REF_DATABASE_ROOT = database.reference
    USER = UserModel()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
}



/*
fun deleteOldUsername(username: String) {
    REF_DATABASE_ROOT
        .child(NODE_USERNAMES)
        .child(username)
        .removeValue()
        .addOnSuccessListener {
            // smth later
        }.addOnFailureListener { showToast(Activity(), it.message.toString()) }
}*/

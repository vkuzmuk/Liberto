package com.vlkuzmuk.freedomcry.database

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.TextView
import androidx.core.view.drawToBitmap
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.databinding.ActivityCreateEventBinding
import com.vlkuzmuk.freedomcry.databinding.ActivityProfileBinding
import com.vlkuzmuk.freedomcry.models.EventModel
import com.vlkuzmuk.freedomcry.utilits.*
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream

class DbManager {

    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback) {
        query.addListenerForSingleValueEvent(AppValueEventListener { snapshot ->
            val eventArray = ArrayList<EventModel>()
            for (item in snapshot.children) {
                var event: EventModel? = null
                //val event = item.children.iterator().next().getValue(eventModel::class.java)
                item.children.forEach {
                    if (event == null) event =
                        it.child(NODE_EVENT).getValue(EventModel()::class.java)
                }
                Log.d(MY_LOG, event?.text!!)
                val reactionCounter = item.child(NODE_LIKES).childrenCount
                val hasReaction =
                    item.child(NODE_LIKES).child(CURRENT_UID).getValue(String::class.java)
                val isPlanned =
                    item.child(NODE_PLANNED).child(CURRENT_UID).getValue(String::class.java)
                event?.isPlanned = isPlanned != null
                event?.hasReaction = hasReaction != null
                event?.reactionCounter = reactionCounter.toString()
                if (event != null) eventArray.add(event!!)
            }
            readDataCallback.readData(eventArray)
        })
    }

    fun getMyEvents(readDataCallback: ReadDataCallback) {
        val query =
            REF_DATABASE_ROOT
                .child(NODE_EVENTS)
                .orderByChild("$CURRENT_UID/uid")
                .equalTo(CURRENT_UID)
        readDataFromDb(query, readDataCallback)
    }

    fun getAllEventsByTimeFirstPage(readDataCallback: ReadDataCallback) {
        val query =
            REF_DATABASE_ROOT
                .child(NODE_EVENTS)
                .orderByChild("/events_time")
                .limitToLast(EVENTS_LIMIT)
        readDataFromDb(query, readDataCallback)
    }

    fun getAllEventsByTimeNextPage(time: String, readDataCallback: ReadDataCallback) {
        val query =
            REF_DATABASE_ROOT
                .child(NODE_EVENTS)
                .orderByChild("/events_time")
                .endBefore(time)
                .limitToLast(EVENTS_LIMIT)
        readDataFromDb(query, readDataCallback)
    }

    fun onLikeClick(event: EventModel, listener: FinishWorkListener) {
        if (event.hasReaction) {
            removeLike(event, listener)
        } else {
            addLike(event, listener)
        }
    }

    private fun addLike(event: EventModel, listener: FinishWorkListener) {
        event.key?.let { key ->
            REF_DATABASE_ROOT
                .child(NODE_EVENTS)
                .child(key)
                .child(NODE_LIKES)
                .child(CURRENT_UID)
                .setValue(CURRENT_UID)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) listener.onFinish()
                }
        }
    }

    private fun removeLike(event: EventModel, listener: FinishWorkListener) {
        event.key?.let {
            REF_DATABASE_ROOT
                .child(NODE_EVENTS)
                .child(it)
                .child(NODE_LIKES)
                .child(CURRENT_UID)
                .removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) listener.onFinish()
                }
        }
    }

    fun onToPlanClick(event: EventModel, listener: FinishWorkListener) {
        if (event.isPlanned) {
            removeFromPlanned(event, listener)
        } else {
            addToPlanned(event, listener)
        }
    }

    private fun addToPlanned(event: EventModel, listener: FinishWorkListener) {
        event.key?.let { key ->
            REF_DATABASE_ROOT
                .child(NODE_EVENTS)
                .child(key)
                .child(NODE_PLANNED)
                .child(CURRENT_UID)
                .setValue(CURRENT_UID)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) listener.onFinish()
                }
        }
    }

    private fun removeFromPlanned(event: EventModel, listener: FinishWorkListener) {
        event.key?.let {
            REF_DATABASE_ROOT
                .child(NODE_EVENTS)
                .child(it)
                .child(NODE_PLANNED)
                .child(CURRENT_UID)
                .removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) listener.onFinish()
                }
        }
    }

    fun saveUsernameAsUID() {
        CURRENT_UID = AUTH.currentUser!!.uid
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_USERNAME)
            .setValue(CURRENT_UID)
    }

    fun downloadUserInfo(activity: Activity, event: EventModel, anonymity: Boolean) {
        val tvUsername: TextView = activity.findViewById(R.id.tvUsername)
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_USERNAME)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val username: String =
                    (it.getValue(USER.username::class.java) ?: USER.username).toString()
                tvUsername.text = username
                event.username = username
                if (!anonymity) event.uid = CURRENT_UID
            })
    }

    fun saveEventToDbWithoutImage(eventKey: String, activity: Activity, event: EventModel) {
        event.photoUrl = "empty"
        val edTextPost: TextView = activity.findViewById(R.id.edTextPost)
        if (edTextPost.text.isNotEmpty()) {
            REF_DATABASE_ROOT
                .child(NODE_EVENTS)
                .child(eventKey)
                .child(CURRENT_UID)
                .child(NODE_EVENT)
                .setValue(event)
                .addOnCompleteListener {
                    REF_DATABASE_ROOT
                        .child(NODE_EVENTS)
                        .child(eventKey)
                        .child(NODE_EVENTS_TIME)
                        .setValue(event.time)
                    activity.finish()
                }
                .addOnFailureListener { e ->
                    showToast(activity, e.message.toString())
                }
        } else edTextPost.error = "Ну скажіть хоть щось, будь ласка =)"
    }

    private fun prepareImage(bitmap: Bitmap): ByteArray {
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        return outStream.toByteArray()
    }

    private fun uploadImage(byteArray: ByteArray, listener: OnCompleteListener<Uri>) {
        val storageRef = Firebase.storage.reference
        val imStorageRef =
            storageRef
                .child(NODE_POST_IMAGES)
                .child(CURRENT_UID)
                .child("image_${System.currentTimeMillis()}")
        val uploadTask = imStorageRef.putBytes(byteArray)
        uploadTask.continueWithTask {
            imStorageRef.downloadUrl
        }.addOnCompleteListener(listener)
    }

    fun uploadImageToDb(
        eventKey: String,
        event: EventModel,
        activity: Activity,
        binding: ActivityCreateEventBinding
    ) {
        val byteArray = prepareImage(binding.imageCreatePost.drawToBitmap())
        uploadImage(byteArray) {
            event.photoUrl = it.result.toString()
            if (binding.edTextPost.text.isNotEmpty()) {
                REF_DATABASE_ROOT
                    .child(NODE_EVENTS)
                    .child(eventKey)
                    .child(CURRENT_UID)
                    .setValue(event)
                    .addOnSuccessListener {
                        activity.finish()
                    }
                    .addOnFailureListener { e ->
                        showToast(activity, e.message.toString())
                    }
            } else binding.edTextPost.error = "Ну скажіть хоть щось, будь ласка =)"
        }
    }

    fun saveSettingsData(activity: Activity) {
        val newFullname = activity.findViewById<TextView>(R.id.edFullname)
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_FULLNAME)
            .setValue(newFullname)
            .addOnSuccessListener {
                showToast(activity, "Успішно збережено!")
            }
            .addOnFailureListener { e ->
                showToast(activity, e.message.toString())
            }
    }

    @SuppressLint("SetTextI18n")
    fun initProfile(binding: ActivityProfileBinding) {
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_USERNAME)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val username: String =
                    (it.getValue(USER.username::class.java) ?: USER.username).toString()
                binding.tvUsername.text = "@$username"
            })

        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_FULLNAME)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val fullname: String =
                    (it.getValue(USER.fullname::class.java) ?: USER.fullname).toString()
                binding.tvUserFullname.text = fullname
            })
    }


    interface ReadDataCallback {
        fun readData(list: ArrayList<EventModel>)
    }

    interface FinishWorkListener {
        fun onFinish()
    }

}
package com.vlkuzmuk.freedomcry.database

import com.google.firebase.database.Query
import com.vlkuzmuk.freedomcry.models.EventModel
import com.vlkuzmuk.freedomcry.utilits.*

class DbManager {

    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback) {
        val eventModel = EventModel()
        query.addListenerForSingleValueEvent(AppValueEventListener {
                val eventArray = ArrayList<EventModel>()
                for (item in it.children) {
                    val event = item.children.iterator().next().getValue(eventModel::class.java)
                    val reactionCounter = item.child(NODE_LIKES).childrenCount
                    val hasReaction =  item.child(NODE_LIKES).child(CURRENT_UID).getValue(String :: class.java)
                    val isPlanned =  item.child(NODE_PLANNED).child(CURRENT_UID).getValue(String :: class.java)
                    event?.isPlanned = isPlanned != null
                    event?.hasReaction = hasReaction != null
                    event?.reactionCounter = reactionCounter.toString()
                    if (event != null) eventArray.add(event)
                }
                readDataCallback.readData(eventArray)
            })
    }

    fun getMyEvents(readDataCallback: ReadDataCallback) {
        val query =
            REF_DATABASE_ROOT
            .child(NODE_EVENTS)
            .orderByChild("$CURRENT_UID/event/uid")
            .equalTo(CURRENT_UID)
        readDataFromDb(query, readDataCallback)
    }

    fun getAllEventsByTime(lastTime: String, readDataCallback: ReadDataCallback) {
        val query =
            REF_DATABASE_ROOT
                .child(NODE_EVENTS)
                .orderByChild("$CURRENT_UID/time")
                .startAfter(lastTime)
                .limitToFirst(EVENTS_LIMIT)
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

    interface ReadDataCallback {
        fun readData(list: ArrayList<EventModel>)
    }

    interface FinishWorkListener {
        fun onFinish()
    }

}
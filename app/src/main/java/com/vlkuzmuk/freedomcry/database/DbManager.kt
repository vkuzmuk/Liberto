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

    interface ReadDataCallback {
        fun readData(list: ArrayList<EventModel>)
    }

}
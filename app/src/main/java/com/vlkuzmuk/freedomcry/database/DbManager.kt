package com.vlkuzmuk.freedomcry.database

import com.vlkuzmuk.freedomcry.models.EventModel
import com.vlkuzmuk.freedomcry.utilits.AppValueEventListener
import com.vlkuzmuk.freedomcry.utilits.NODE_EVENTS
import com.vlkuzmuk.freedomcry.utilits.REF_DATABASE_ROOT

class DbManager {

    fun readDataFromDb(readDataCallback: ReadDataCallback) {
        val eventModel = EventModel()
        REF_DATABASE_ROOT.child(NODE_EVENTS)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val eventArray = ArrayList<EventModel>()
                for (item in it.children) {
                    val event = item.children.iterator().next().getValue(eventModel::class.java)
                    if (event != null) eventArray.add(event)
                }
                readDataCallback.readData(eventArray)
            })
    }
    
    interface ReadDataCallback {
        fun readData(list: ArrayList<EventModel>)
    }

}
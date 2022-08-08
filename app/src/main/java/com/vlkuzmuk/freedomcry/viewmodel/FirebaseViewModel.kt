package com.vlkuzmuk.freedomcry.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vlkuzmuk.freedomcry.database.DbManager
import com.vlkuzmuk.freedomcry.models.EventModel

class FirebaseViewModel: ViewModel() {
    private val dbManager = DbManager()
    val liveEventData = MutableLiveData<ArrayList<EventModel>>()

    fun loadAllEventsByTime(lastTime: String) {
        dbManager.getAllEventsByTime(lastTime, object : DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<EventModel>) {
                liveEventData.value = list
            }
        })
    }

    fun loadMyEvents() {
        dbManager.getMyEvents(object : DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<EventModel>) {
                liveEventData.value = list
            }
        })
    }

    fun onReactionClick(event: EventModel, reaction: Int) {
        dbManager.onReactionClick(event, reaction, object : DbManager.FinishWorkListener{
            override fun onFinish() {
                val updateList = liveEventData.value
                val position = updateList?.indexOf(event)
                if (position != -1) {
                    position?.let {
                        updateList[position] = updateList[position].copy(hasReaction = !event.hasReaction)
                    }
                }
                liveEventData.postValue(updateList!!)
            }
        })
    }


}
package com.vlkuzmuk.freedomcry.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vlkuzmuk.freedomcry.database.DbManager
import com.vlkuzmuk.freedomcry.models.EventModel

class FirebaseViewModel : ViewModel() {
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

    fun onLikeClick(event: EventModel) {
        dbManager.onLikeClick(event, object : DbManager.FinishWorkListener {
            override fun onFinish() {
                val updateList = liveEventData.value
                val position = updateList?.indexOf(event)
                if (position != -1) {
                    position?.let {
                        val reactionCounter =
                            if (event.hasReaction)
                                event.reactionCounter.toInt() - 1
                            else
                                event.reactionCounter.toInt() + 1
                        updateList[position] = updateList[position].copy(
                                hasReaction = !event.hasReaction,
                                reactionCounter = reactionCounter.toString()
                            )
                    }
                }
                liveEventData.postValue(updateList!!)
            }
        })
    }

    fun onToPlanClick(event: EventModel) {
        dbManager.onToPlanClick(event, object : DbManager.FinishWorkListener {
            override fun onFinish() {
                val updateList = liveEventData.value
                val position = updateList?.indexOf(event)
                if (position != -1) {
                    position?.let {
                        updateList[position] = updateList[position].copy(
                            isPlanned = !event.isPlanned,
                        )
                    }
                }
                liveEventData.postValue(updateList!!)
            }
        })
    }



}
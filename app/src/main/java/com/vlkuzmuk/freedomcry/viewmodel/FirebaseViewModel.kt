package com.vlkuzmuk.freedomcry.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vlkuzmuk.freedomcry.database.DbManager
import com.vlkuzmuk.freedomcry.models.EventModel

class FirebaseViewModel: ViewModel() {
    private val dbManager = DbManager()
    val lifeEventData = MutableLiveData<ArrayList<EventModel>>()

    fun loadAllEvents() {
        dbManager.readDataFromDb(object : DbManager.ReadDataCallback {
            override fun readData(list: ArrayList<EventModel>) {
                lifeEventData.value = list
            }

        })
    }

}
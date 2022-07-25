package com.vlkuzmuk.freedomcry.adapters

import androidx.recyclerview.widget.DiffUtil
import com.vlkuzmuk.freedomcry.models.EventModel

class DiffUtilHelper(
    private val oldList: List<EventModel>,
    private val newList: List<EventModel>
    ) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].key == newList[newItemPosition].key
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
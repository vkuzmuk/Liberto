package com.vlkuzmuk.freedomcry.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.activities.bottomNavActivities.EventActivity
import com.vlkuzmuk.freedomcry.databinding.ItemPostBinding
import com.vlkuzmuk.freedomcry.models.EventModel

class EventAdapter(private val activity: EventActivity) : RecyclerView.Adapter<EventAdapter.EventHolder>() {
    private val eventArray = ArrayList<EventModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventHolder(binding, activity)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.setData(eventArray[position])
    }

    override fun getItemCount(): Int {
        return eventArray.size
    }

    fun updateAdapter(newList: List<EventModel>) {
        val tempArray = ArrayList<EventModel>()
        tempArray.addAll(eventArray)
        tempArray.addAll(newList)
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(eventArray, tempArray))
        diffResult.dispatchUpdatesTo(this)
        eventArray.clear()
        eventArray.addAll(tempArray)
    }

    fun updateAdapterWithClear(newList: List<EventModel>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(eventArray, newList))
        diffResult.dispatchUpdatesTo(this)
        eventArray.clear()
        eventArray.addAll(newList)
    }


    class EventHolder(
        private val binding: ItemPostBinding,
        private val activity: EventActivity)
        : RecyclerView.ViewHolder(binding.root) {

        fun setData(event: EventModel) {
            binding.apply {
                tvUsernamePost.text = event.username
                tvTextPost.text = event.post_text
                if (event.hasReaction) btnLike.setImageResource(R.drawable.ic_like_pressed)
                else btnLike.setImageResource(R.drawable.ic_like_passive)
                btnLike.setOnClickListener { activity.onReactionClicked(event) }
                btnDislike.setOnClickListener { }
                if (event.photoUrl != "empty") {
                    imagePost.visibility = View.VISIBLE
                    Picasso.get().load(event.photoUrl).into(imagePost)
                }
            }
        }
        interface Listener {
            fun onReactionClicked(event: EventModel)
        }
    }
}
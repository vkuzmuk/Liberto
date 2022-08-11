package com.vlkuzmuk.freedomcry.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.activities.bottomNavActivities.EventActivity
import com.vlkuzmuk.freedomcry.databinding.ItemPostBinding
import com.vlkuzmuk.freedomcry.models.EventModel
import com.vlkuzmuk.freedomcry.utilits.APP_ACTIVITY

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
                checkReaction(event)
                checkPlanned(event)
                tvLikeCounter.text = event.reactionCounter
                btnLike.setOnClickListener { activity.onLikeClicked(event) }
                btnToPlan.setOnClickListener { activity.onToPlanClicked(event) }
                if (event.photoUrl != "empty") {
                    imagePost.visibility = View.VISIBLE
                    Picasso.get().load(event.photoUrl).into(imagePost)
                }
            }
        }

        private fun checkReaction(event: EventModel) = with(binding) {
            if (event.hasReaction) btnLike.setImageResource(R.drawable.ic_like_pressed)
            else btnLike.setImageResource(R.drawable.ic_like_passive)
        }

        private fun checkPlanned(event: EventModel) = with(binding) {
            if (event.isPlanned) {
                btnToPlan.text = activity.getString(R.string.planned)
                btnToPlan.setTextColor(ContextCompat.getColor(activity, R.color.white))
                btnToPlan.setBackgroundResource(R.drawable.bg_pressed_btn_to_plan)
            } else {
                btnToPlan.text = activity.getString(R.string.toPlan)
                btnToPlan.setTextColor(ContextCompat.getColor(activity, R.color.blue_main))
                btnToPlan.setBackgroundResource(R.drawable.bg_normal_btn_to_plan)
            }
        }

        interface Listener {
            fun onLikeClicked(event: EventModel)
            fun onToPlanClicked(event: EventModel)
        }
    }
}
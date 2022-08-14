package com.vlkuzmuk.freedomcry.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.activities.bottomNavActivities.EventsActivity
import com.vlkuzmuk.freedomcry.databinding.ItemPostBinding
import com.vlkuzmuk.freedomcry.models.EventModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventAdapter(private val activity: EventsActivity) : RecyclerView.Adapter<EventAdapter.EventHolder>() {
    private val eventArray = ArrayList<EventModel>()
    private var timeFormatter: SimpleDateFormat? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventHolder(binding, activity, timeFormatter!!)
    }

    init {
        timeFormatter = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
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
        private val activity: EventsActivity,
        private val formatter: SimpleDateFormat)
        : RecyclerView.ViewHolder(binding.root) {

        fun setData(event: EventModel) {
            binding.apply {
                tvUsernameEvent.text = event.username
                tvTextEvent.text = event.text
                tvTitleEvent.text = event.title
                checkReaction(event)
                checkPlanned(event)
                tvTimeEvent.text = getTimeFromMillis(event.time)
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

        private fun getTimeFromMillis(timeMillis: String): String {
            val c = Calendar.getInstance()
            c.timeInMillis = timeMillis.toLong()
            return formatter.format(c.time)

        }

        interface Listener {
            fun onLikeClicked(event: EventModel)
            fun onToPlanClicked(event: EventModel)
        }
    }
}
package com.vlkuzmuk.freedomcry.activities.bottomNavActivities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.activities.CreateEventActivity
import com.vlkuzmuk.freedomcry.adapters.EventAdapter
import com.vlkuzmuk.freedomcry.databinding.ActivityEventsBinding
import com.vlkuzmuk.freedomcry.models.EventModel
import com.vlkuzmuk.freedomcry.utilits.SCROLL_DOWN
import com.vlkuzmuk.freedomcry.utilits.initFirebase
import com.vlkuzmuk.freedomcry.viewmodel.FirebaseViewModel

class EventActivity : AppCompatActivity(), EventAdapter.EventHolder.Listener {
    private lateinit var binding: ActivityEventsBinding
    private val adapter = EventAdapter(this)
    private val firebaseViewModel: FirebaseViewModel by viewModels()
    private var clearUpdate: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
        initFirebase()
        bottomNavigationView()
        initRecyclerView()
        initViewModel()
        scrollListener()
        firebaseViewModel.loadAllEventsByTime("0")
    }

    private fun onClick() {
        binding.fbCreateEvent.setOnClickListener {
            val i = Intent(this, CreateEventActivity::class.java)
            startActivity(i)
        }
    }

    private fun initViewModel() {
        firebaseViewModel.liveEventData.observe(this) {
            if (!clearUpdate) {
                adapter.updateAdapter(it)
            } else {
                adapter.updateAdapterWithClear(it)
            }
            binding.tvIsAnyEvents.visibility =
                if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            rcViewEvents.layoutManager = LinearLayoutManager(this@EventActivity)
            rcViewEvents.adapter = adapter
        }
    }

    private fun scrollListener() = with(binding) {
        rcViewEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(SCROLL_DOWN) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    clearUpdate = false
                    val eventList = firebaseViewModel.liveEventData.value!!
                    if (eventList.isNotEmpty()) {
                        eventList[eventList.size - 1].let {
                            pbDownloadMoreEvents.visibility = View.VISIBLE
                            firebaseViewModel.loadAllEventsByTime(it.time)
                            pbDownloadMoreEvents.visibility = View.GONE
                        }
                    }
                }
                refreshPage.setColorSchemeResources(R.color.blue_main)
                refreshPage.setOnRefreshListener {
                    clearUpdate = true
                    firebaseViewModel.loadAllEventsByTime("0")
                    refreshPage.isRefreshing = false
                }
            }
        })
    }

    private fun bottomNavigationView() {
        val bottomNav = binding.bottomNavigationView
        bottomNav.selectedItemId = R.id.bottomEvents
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottomEvents -> {
                    val i = Intent(this, EventActivity::class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(0, 0)
                }
                R.id.bottomEventManager -> {
                    val i = Intent(this, EventManagerActivity::class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(0, 0)
                }
                R.id.bottomChats -> {
                    val i = Intent(this, ChatActivity::class.java)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                }
                R.id.bottomProfile -> {
                    val i = Intent(this, ProfileActivity::class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(0, 0)
                }
            }
            true
        }
    }

    override fun onLikeClicked(event: EventModel) {
        firebaseViewModel.onLikeClick(event)
    }

    override fun onToPlanClicked(event: EventModel) {
        firebaseViewModel.onToPlanClick(event)
    }
}
package com.vlkuzmuk.freedomcry.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.MessageQueue
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vlkuzmuk.freedomcry.adapters.EventAdapter
import com.vlkuzmuk.freedomcry.databinding.FragmentNewsBinding
import com.vlkuzmuk.freedomcry.utilits.MY_LOG
import com.vlkuzmuk.freedomcry.utilits.SCROLL_DOWN
import com.vlkuzmuk.freedomcry.viewmodel.FirebaseViewModel

class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private val adapter = EventAdapter()
    private val firebaseViewModel: FirebaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        initRecyclerView()
        initViewModel()
        scrollListener()
        firebaseViewModel.loadAllEvents()
        return binding.root
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initViewModel() {
        firebaseViewModel.lifeEventData.observe(this) {
            adapter.updateAdapter(it)
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            rcViewEvents.layoutManager = LinearLayoutManager(context)
            rcViewEvents.adapter = adapter
        }
    }

    private fun scrollListener() = with(binding) {
        rcViewEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(SCROLL_DOWN) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(MY_LOG, "Can not scroll down")
                }
            }
        })
    }


}
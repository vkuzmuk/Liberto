package com.vlkuzmuk.freedomcry.activities.bottomNavActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vlkuzmuk.freedomcry.databinding.ActivityEventManagerBinding
import com.vlkuzmuk.freedomcry.utilits.PAGE_EVENTS_MANAGER
import com.vlkuzmuk.freedomcry.utilits.bottomNav

class EventManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNav(this, this, PAGE_EVENTS_MANAGER)
    }

}
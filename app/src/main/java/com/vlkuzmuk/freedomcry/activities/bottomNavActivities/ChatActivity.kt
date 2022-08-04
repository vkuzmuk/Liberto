package com.vlkuzmuk.freedomcry.activities.bottomNavActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.databinding.ActivityChatBinding
import com.vlkuzmuk.freedomcry.utilits.replaceActivity

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView()
    }

    private fun bottomNavigationView() {
        val bottomNav = binding.bottomNavigationView
        bottomNav.selectedItemId = R.id.bottomChats
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottomEvents -> {
                    val i = Intent(this, EventActivity :: class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(0,0)
                }
                R.id.bottomEventManager -> {
                    val i = Intent(this, EventManagerActivity :: class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(0,0)
                }
                R.id.bottomChats -> {
                    val i = Intent(this, ChatActivity :: class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(0,0)
                }
                R.id.bottomProfile -> {
                    val i = Intent(this, ProfileActivity :: class.java)
                    startActivity(i)
                    finish()
                    overridePendingTransition(0,0)
                }
            }
            true
        }
    }


}
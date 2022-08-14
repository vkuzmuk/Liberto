package com.vlkuzmuk.freedomcry.activities.bottomNavActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vlkuzmuk.freedomcry.databinding.ActivityChatBinding
import com.vlkuzmuk.freedomcry.utilits.PAGE_CHATS
import com.vlkuzmuk.freedomcry.utilits.bottomNav

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNav(this, this, PAGE_CHATS)
    }


}
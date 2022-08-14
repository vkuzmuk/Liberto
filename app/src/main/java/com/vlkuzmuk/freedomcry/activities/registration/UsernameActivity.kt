package com.vlkuzmuk.freedomcry.activities.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vlkuzmuk.freedomcry.R
import com.vlkuzmuk.freedomcry.activities.bottomNavActivities.EventsActivity
import com.vlkuzmuk.freedomcry.databinding.ActivityUsernameBinding
import com.vlkuzmuk.freedomcry.utilits.*
import java.util.*

class UsernameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUsernameBinding
    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFirebase()

        binding.btnSaveUsername.setOnClickListener {
            checkIfUserExists()
        }
    }

    private fun checkIfUserExists() {
        username = binding.edUsername.text.toString().lowercase(Locale.getDefault())
        if (username.isEmpty()) {
            binding.edUsername.error = getString(R.string.please_enter_username)
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(username)) {
                        binding.edUsername.error = getString(R.string.username_already_is_used)
                    } else {
                        saveUsername(username)
                    }
                })
        }
    }

    private fun saveUsername(username: String) {
        REF_DATABASE_ROOT
            .child(NODE_USERNAMES)
            .child(username)
            .setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername(username)
                }
            }
    }

    private fun updateCurrentUsername(username: String) {
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_USERNAME)
            .setValue(username)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(this, getString(R.string.welcome_to_the_board))
                    startActivity(Intent(this, EventsActivity::class.java))
                    finish()
                    //deleteOldUsername(username)
                } else {
                    showToast(this, it.exception?.message.toString())
                }
            }
    }


}
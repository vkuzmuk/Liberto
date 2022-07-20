package com.vlkuzmuk.freedomcry.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.vlkuzmuk.freedomcry.activities.registration.LoginActivity
import com.vlkuzmuk.freedomcry.activities.MainActivity
import com.vlkuzmuk.freedomcry.activities.SettingsActivity
import com.vlkuzmuk.freedomcry.adapters.PagerAdapter
/*import com.vlkuzmuk.freedomcry.database.ReadDataCallback*/
import com.vlkuzmuk.freedomcry.databinding.FragmentProfileBinding
import com.vlkuzmuk.freedomcry.fragments.tabs.FollowersFragment
import com.vlkuzmuk.freedomcry.fragments.tabs.LikedFragment
import com.vlkuzmuk.freedomcry.models.EventModel
import com.vlkuzmuk.freedomcry.utilits.*

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var tabs: TabLayout
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        initProfile()


        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }


        binding.btnSettings.setOnClickListener {
            val fullname = binding.tvUserFullname.text.toString()
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra("fullname", fullname)
            startActivity(intent)

        }

        viewPager()

        return binding.root
    }


    private fun viewPager() {
        APP_ACTIVITY = MainActivity()
        tabs = binding.tabs
        viewPager = binding.viewPager
        pagerAdapter = PagerAdapter(childFragmentManager)
        APP_ACTIVITY.supportFragmentManager == childFragmentManager
        // set fragment list
        pagerAdapter.addFragment(FollowersFragment(), "Відстежування")
        pagerAdapter.addFragment(LikedFragment(), "Вподобання")
        // set adapter
        viewPager.adapter = pagerAdapter
        // set tabs
        tabs.setupWithViewPager(viewPager)

    }


    @SuppressLint("SetTextI18n")
    private fun initProfile() {
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_USERNAME)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val username: String =
                    (it.getValue(USER.username::class.java) ?: USER.username).toString()
                binding.tvProfile.text = "@$username"
            })

        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_FULLNAME)
            .addListenerForSingleValueEvent(AppValueEventListener {
                val fullname: String =
                    (it.getValue(USER.fullname::class.java) ?: USER.fullname).toString()
                binding.tvUserFullname.text = fullname
            })
    }

    override fun onResume() {
        super.onResume()
        getDataFromSettings()
    }

    private fun getDataFromSettings() {
        val newFullname = activity?.intent?.getStringExtra("newFullname").toString()
        Log.d("MyLog", newFullname)
    }

}

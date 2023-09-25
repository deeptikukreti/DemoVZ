package com.example.demovz.ui.activity.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.demovz.R
import com.example.demovz.adapter.eventsAdapter.EventPagerAdapter
import com.example.demovz.databinding.ActivityEventListBinding
import com.example.demovz.ui.fragments.activityBasedEvent.ActivityBasedEventFragment
import com.example.demovz.ui.fragments.timeBasedEvent.TimeBasedEventFragment
import com.google.android.material.tabs.TabLayoutMediator

class EventListActivity : AppCompatActivity() {
    private var _binding: ActivityEventListBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEventListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewInitialization()
    }

    private fun viewInitialization() {
        binding.appBar.apply {
            ivLogo.visibility = View.GONE
            backIcon.visibility = View.VISIBLE
            txtTitle.text = getString(R.string.events_list)

            backIcon.setOnClickListener {
                onBackPressed()
            }
        }

        binding.addBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@EventListActivity,
                    CreateEventActivity::class.java
                )
            )
        }

        val adapter = EventPagerAdapter(this@EventListActivity)
        adapter.addFragment(TimeBasedEventFragment(), "Time Based")
        adapter.addFragment(ActivityBasedEventFragment(), "Activity Based")

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 0
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

    }
}
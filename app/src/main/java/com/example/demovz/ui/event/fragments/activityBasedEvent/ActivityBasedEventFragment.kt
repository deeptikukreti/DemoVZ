package com.example.demovz.ui.event.fragments.activityBasedEvent

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.demovz.ui.event.adapter.eventsListAdapter.GroupListAdapter
import com.example.demovz.databinding.FragmentActivityBasedEventBinding
import com.example.demovz.db.events.Event
import com.example.demovz.db.events.RoomDb
import com.example.demovz.ui.event.activity.EventDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityBasedEventFragment : Fragment(), GroupListAdapter.OnItemClickListener {
    private lateinit var viewModel: ActivityBasedEventViewModel
    private var _binding: FragmentActivityBasedEventBinding? = null
    private val binding get() = _binding!!
    private var groupList = ArrayList<Event>()
    private lateinit var grpAdapter: GroupListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ActivityBasedEventViewModel::class.java]
        _binding = FragmentActivityBasedEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        grpAdapter = GroupListAdapter(groupList).apply { setOnClickListener(this@ActivityBasedEventFragment
        ) }
        binding.rvGrp.adapter = grpAdapter

        CoroutineScope(Dispatchers.IO).launch { getGroupDataList() }

        return root
    }

    private suspend fun getGroupDataList() {
        lifecycleScope.launch {
            RoomDb.getInstance(requireContext()).eventDao().getEventByTrigger(2).collect { eventsList ->
                if (eventsList.isNotEmpty()) {
                    grpAdapter.submitList(eventsList)
                }
            }
        }
    }

    override fun onClicked(event: Event) {
        startActivity(Intent(activity, EventDetailActivity::class.java).putExtra("ID", event.id))
    }

}
package com.example.demovz.ui.event.fragments.timeBasedEvent

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.demovz.ui.event.adapter.eventsListAdapter.GroupListAdapter
import com.example.demovz.databinding.FragmentTimeBasedEventBinding
import com.example.demovz.db.entity.Event
import com.example.demovz.ui.event.activity.EventDetailActivity
import com.example.demovz.ui.event.viewModel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimeBasedEventFragment : Fragment(), GroupListAdapter.OnItemClickListener{
    private val eventViewModel: EventViewModel by  activityViewModels()
    private var _binding: FragmentTimeBasedEventBinding? = null
    private val binding get() = _binding!!
    private var groupList = ArrayList<Event>()
    private lateinit var grpAdapter: GroupListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeBasedEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        grpAdapter = GroupListAdapter(groupList).apply { setOnClickListener(this@TimeBasedEventFragment
        ) }
        binding.rvGrp.adapter = grpAdapter
        getGroupDataList()
        return root
    }

    private fun getGroupDataList() {
        eventViewModel.getEventsByTriggerType(1).observe(requireActivity(), Observer {
            it?.let {
                grpAdapter.submitList(it)
            }
        })
    }

    override fun onClicked(event: Event) {
        startActivity(Intent(activity, EventDetailActivity::class.java).putExtra("ID", event.id))
    }
}
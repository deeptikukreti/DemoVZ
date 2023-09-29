package com.example.demovz.ui.event.fragments.activityBasedEvent

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.demovz.ui.event.adapter.eventsListAdapter.GroupListAdapter
import com.example.demovz.databinding.FragmentActivityBasedEventBinding
import com.example.demovz.db.entity.Event
import com.example.demovz.ui.event.activity.EventDetailActivity
import com.example.demovz.ui.event.viewModel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityBasedEventFragment : Fragment(), GroupListAdapter.OnItemClickListener {
    private val eventViewModel: EventViewModel by activityViewModels()
    private var _binding: FragmentActivityBasedEventBinding? = null
    private val binding get() = _binding!!
    private var groupList = ArrayList<Event>()
    private lateinit var grpAdapter: GroupListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityBasedEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        grpAdapter = GroupListAdapter(groupList).apply {
            setOnClickListener(
                this@ActivityBasedEventFragment
            )
        }
        binding.rvGrp.adapter = grpAdapter

        getGroupDataList()
        return root
    }

    private fun getGroupDataList() {
        eventViewModel.getEventsByTriggerType(2).observe(requireActivity(), Observer {
            if (it.isEmpty()) {
                binding.txtEvntName.visibility = View.VISIBLE
            } else {
                binding.txtEvntName.visibility = View.GONE
                grpAdapter.submitList(it)
            }
        })
    }

    override fun onClicked(event: Event) {
        startActivity(Intent(activity, EventDetailActivity::class.java).putExtra("ID", event.id))
    }

    override fun onResume() {
        super.onResume()
        getGroupDataList()
    }

}
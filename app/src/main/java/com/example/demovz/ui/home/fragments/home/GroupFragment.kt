package com.example.demovz.ui.home.fragments.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.demovz.R
import com.example.demovz.ui.event.activity.CreateEventActivity
import com.example.demovz.ui.event.adapter.eventsListAdapter.GroupListAdapter
import com.example.demovz.databinding.FragmentGroupBinding
import com.example.demovz.db.entity.Event
import com.example.demovz.ui.event.activity.EventDetailActivity
import com.example.demovz.ui.event.activity.EventListActivity
import com.example.demovz.ui.event.viewModel.EventViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupFragment : Fragment(), GroupListAdapter.OnItemClickListener {

    private var _binding: FragmentGroupBinding? = null
    private val viewModel: EventViewModel by  viewModels()
    private val binding get() = _binding!!
    private var groupList = ArrayList<Event>()
    private lateinit var grpAdapter: GroupListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.appBar.txtTitle.text = getString(R.string.home)
        grpAdapter = GroupListAdapter(groupList).apply { setOnClickListener(this@GroupFragment) }
        binding.rvGrp.adapter = grpAdapter
        binding.addBtn.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    CreateEventActivity::class.java
                )
            )
        }

        binding.txtGrp.setOnClickListener {
            startActivity(Intent(requireActivity(), EventListActivity::class.java))
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        getGroupDataList()
    }

    private fun getGroupDataList() {
        viewModel.getAllEvents().observe(this) {
            it?.let {
                grpAdapter.submitList(it)
            }
        }
    }

    override fun onClicked(event: Event) {
        startActivity(Intent(activity, EventDetailActivity::class.java).putExtra("ID", event.id))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
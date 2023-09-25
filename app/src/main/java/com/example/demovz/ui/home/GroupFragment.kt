package com.example.demovz.ui.home

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.demovz.ui.activity.event.CreateEventActivity
import com.example.demovz.adapter.GroupListAdapter
import com.example.demovz.databinding.FragmentGroupBinding
import com.example.demovz.db.events.Event
import com.example.demovz.db.events.RoomDb
import com.example.demovz.ui.activity.event.EditEventActivity
import com.example.demovz.ui.activity.event.EventDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupFragment : Fragment(), GroupListAdapter.OnItemClickListener {

    private lateinit var viewModel: GroupViewModel
    private var _binding: FragmentGroupBinding? = null
    private val binding get() = _binding!!
    private var groupList = ArrayList<Event>()
    private lateinit var grpAdapter: GroupListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[GroupViewModel::class.java]
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding?.appBar?.txtTitle?.text = "Home"
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

        return root
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch { getGroupDataList() }
    }

    private suspend fun getGroupDataList() {
        lifecycleScope.launch {
            RoomDb.getInstance(requireContext()).eventDao().getAllEvents().collect { eventsList ->
                if (eventsList.isNotEmpty()) {
                    grpAdapter.submitList(eventsList)
                }
            }
        }
    }


    override fun onClicked(event: Event) {
        startActivity(Intent(activity, EventDetailActivity::class.java).putExtra("ID", event.id))
    }

    override fun onItemRemoved(item: Event) {
        activity?.let {
            RoomDb.getInstance(
                it.applicationContext
            ).eventDao().delete(event = item)
        }

    }

    override fun onItemEdited(position: Int, id: Int) {
        startActivity(Intent(activity, EditEventActivity::class.java).putExtra("ID", id))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
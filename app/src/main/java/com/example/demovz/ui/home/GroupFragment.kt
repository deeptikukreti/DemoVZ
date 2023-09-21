package com.example.demovz.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.demovz.ui.activity.CreateEventActivity
import com.example.demovz.R
import com.example.demovz.adapter.GroupListAdapter
import com.example.demovz.databinding.FragmentGroupBinding
import com.example.demovz.db.Event
import com.example.demovz.db.RoomDb
import com.example.demovz.ui.activity.EditEventActivity
import com.example.demovz.ui.activity.MainActivity
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

        CoroutineScope(Dispatchers.IO).launch { getGroupDataList() }

        return root
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

    @SuppressLint("NotifyDataSetChanged")
    private fun createAlertDialog() {
        val builder = AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.create_event_layout, null)
        val eventName = view.findViewById<EditText>(R.id.edt_event_name)
        val next = view.findViewById<Button>(R.id.btn_next)
        val cancel = view.findViewById<Button>(R.id.btn_cancel)
        builder.setView(view)
        next.setOnClickListener {
            //groupList.add(eventName.text.toString())
            grpAdapter.notifyDataSetChanged()
            builder.dismiss()
            startActivity(
                Intent(
                    requireActivity(),
                    CreateEventActivity::class.java
                ).putExtra("EVENT_NAME", eventName.text.toString())
            )
        }
        cancel.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    override fun onClicked(name: String) {
//        startActivity(Intent(this,GroupDetailActivity::class.java).putExtra("GROUP_NAME",groupName))
    }

    override fun onItemRemoved(item:Event) {
        activity?.let {
            RoomDb.getInstance(
                it.applicationContext
            ).eventDao().delete(event = item)
        }

    }

    override fun onItemEdited(position: Int, id: Int) {
        startActivity(Intent(activity,EditEventActivity::class.java).putExtra("ID",id))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
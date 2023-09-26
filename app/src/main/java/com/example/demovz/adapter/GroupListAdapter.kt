package com.example.demovz.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.databinding.GroupItemLayoutBinding
import com.example.demovz.db.events.Event

class GroupListAdapter(
    private var groupList: ArrayList<Event>,
) : RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {
    private lateinit var listener: OnItemClickListener

    inner class ViewHolder(val binding: GroupItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GroupItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(groupList[position].eventName) {
                binding.groupName.text = this
                binding.words.text = if (this.isNotBlank()) {
                    first().toString()
                } else ""
            }
            with(groupList[position]) {
                if(this.triggerType==1)
                binding.groupDetails.text = "${this.dateTime} Recurring: ${this.isRecurring}"
                else
                binding.groupDetails.text = "Sensor Device : ${this.sensorDevice}"

                binding.clGroup.setOnClickListener {
                    listener.onClicked(this)
                }
            }

        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return groupList.size
    }

    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(eventsList: List<Event>) {
        this.groupList = eventsList as ArrayList<Event>
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClicked(event: Event)
    }
}
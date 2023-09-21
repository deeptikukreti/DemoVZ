package com.example.demovz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.databinding.GroupItemLayoutBinding

class GroupListAdapter(
    private var groupList: ArrayList<String>,
) : RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {
    private lateinit var listener: OnItemClickListener

    inner class ViewHolder(val binding: GroupItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GroupItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(groupList[position]) {
                binding.groupName.text = this
                binding.words.text  = if(this.isNotBlank()) { first().toString() } else ""
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

    interface OnItemClickListener {
        fun onClicked(name: String)
    }
}
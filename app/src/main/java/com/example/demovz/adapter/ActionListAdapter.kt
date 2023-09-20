package com.example.demovz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.databinding.ActionItemLayoutBinding

class ActionListAdapter(
    var devicesList: ArrayList<String>,
) : RecyclerView.Adapter<ActionListAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ActionItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActionItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(devicesList[position]){
                binding.txtActionName.text = this
                binding.toggleBtn.setOnClickListener {
                    listener.onClicked()
                }
                binding.cancelImg.setOnClickListener {
                    listener.onActionRemoved(position)
                }

                binding.editImg.setOnClickListener {
                    listener.onActionEdit(position)
                }
            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return devicesList.size
    }
    private lateinit var listener: OnItemClickListener
    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    interface OnItemClickListener {
        fun onClicked()
        fun onActionRemoved(position:Int)
        fun onActionEdit(position: Int)
    }

}
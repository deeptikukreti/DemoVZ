package com.example.demovz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.databinding.DeviceItemLayoutBinding

class DevicesListAdapter(
    var devicesList: ArrayList<String>,
) : RecyclerView.Adapter<DevicesListAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: DeviceItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DeviceItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(devicesList[position]){
                binding.txtDeviceName.text = this
                binding.txtDeviceName.setOnClickListener {
                    listener.onClicked(this)
                }
                binding.cancelImg.setOnClickListener {
                    listener.onDeviceRemoved(position)
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
        fun onClicked(s:String)
        fun onDeviceRemoved(position: Int)
    }

}
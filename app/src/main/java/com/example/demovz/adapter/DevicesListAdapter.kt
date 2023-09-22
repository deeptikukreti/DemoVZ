package com.example.demovz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.databinding.DeviceItemLayoutBinding
import com.example.demovz.db.Device

class DevicesListAdapter(
    private var devicesList: ArrayList<Device>,
    private var isEventDetail: Boolean
) : RecyclerView.Adapter<DevicesListAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: DeviceItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DeviceItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.apply {
                with(devicesList[position]) {

                    txtDeviceName.text = this.deviceName
                    toggleBtn.text = this.action

                    txtDeviceName.setOnClickListener {
                        listener.onClicked(this.deviceName)
                    }

                    if (isEventDetail) {
                        cancelImg.visibility = View.GONE
                        toggleBtn.isClickable = false
                        toggleBtn.isEnabled = false
                    }
                    cancelImg.setOnClickListener {
                        listener.onDeviceRemoved(position)
                    }

                    toggleBtn.setOnClickListener {
                        listener.onToggleClicked(
                            this.deviceName,
                            toggleBtn.text.toString(),
                            position
                        )
                    }
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
        fun onClicked(s: String)
        fun onToggleClicked(s: String, action: String, position: Int)
        fun onDeviceRemoved(position: Int)
    }

}
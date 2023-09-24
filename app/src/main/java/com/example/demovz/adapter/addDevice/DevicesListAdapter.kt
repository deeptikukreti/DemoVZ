package com.example.demovz.adapter.addDevice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.databinding.DeviceDetailItemLayoutBinding
import com.example.demovz.databinding.DeviceItemLayoutBinding
import com.example.demovz.db.events.Device

class DevicesListAdapter(
    private var devicesList: ArrayList<Device>,
    private var isEventDetail: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolder(val binding: DeviceItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class DeviceDetailViewHolder(val binding: DeviceDetailItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (isEventDetail) {
            val view =
                DeviceDetailItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            DeviceDetailViewHolder(view)
        } else {
            val view =
                DeviceItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(view)
        }
//        val binding =
//            DeviceItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//
//        return ViewHolder(binding)
    }

//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isEventDetail) {
            with(holder as DeviceDetailViewHolder) {
                binding.apply {
                    txtDeviceName.text =
                        "${devicesList[position].deviceName} : ${devicesList[position].action.toUpperCase()}"
                }
            }
        } else {
            with(holder as ViewHolder) {
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
                            listener.onDeviceRemoved(adapterPosition)
                        }

                        toggleBtn.setOnClickListener {
                            listener.onToggleClicked(
                                this.deviceName,
                                toggleBtn.text.toString(),
                                adapterPosition
                            )
                        }
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
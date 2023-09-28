package com.example.demovz.ui.event.adapter.addDevice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.databinding.SelectDeviceItemLayoutBinding
import com.example.demovz.db.model.Device

class SelectDevicesListAdapter(
) : RecyclerView.Adapter<SelectDevicesListAdapter.ViewHolder>() {
    private var devicesList = ArrayList<Device>()

    inner class ViewHolder(val binding: SelectDeviceItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SelectDeviceItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.apply {
                with(devicesList[position]) {
                    txtDeviceName.text = "${this.deviceName}"
                    cbSelectDevice.isChecked = this.isSelected

                    cbSelectDevice.setOnClickListener {
                        devicesList[adapterPosition].isSelected=cbSelectDevice.isChecked
                        listener.onClicked(devicesList[adapterPosition], cbSelectDevice.isChecked, adapterPosition)
                    }
//                    cbSelectDevice.setOnCheckedChangeListener { _, isChecked ->
//                        devicesList[adapterPosition].device.isSelected=isChecked
//                        listener.onClicked(this, isChecked, adapterPosition)
//                    }
                }
            }
        }

    }

    fun addList(list: ArrayList<Device>) {
        devicesList.clear()
        devicesList.addAll(list)

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
        fun onClicked(i: Device, isChecked: Boolean, position: Int)
    }

}

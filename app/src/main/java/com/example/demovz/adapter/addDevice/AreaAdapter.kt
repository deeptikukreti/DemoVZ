package com.example.demovz.adapter.addDevice

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.databinding.AreaItemLayoutBinding
import com.example.demovz.db.devices.AreaWithDeviceData

class AreaAdapter(
    private var areaList: ArrayList<AreaWithDeviceData>,
    val isEventDetail :Boolean
) : RecyclerView.Adapter<AreaAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: AreaItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AreaItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (areaList[position].deviceList.isEmpty())
            holder.binding.cardViewMain.visibility = View.GONE
        else {
            with(holder) {
                binding.apply {
                    with(areaList[position]) {
                        if (this.deviceList.isEmpty())
                            cardViewMain.visibility = View.GONE
                        else
                            cardViewMain.visibility = View.VISIBLE

                        tvAreaName.text = this.areaName
                        val deviceAdapter = DevicesListAdapter(this.deviceList, isEventDetail)
                        deviceAdapter.setOnClickListener(object :
                            DevicesListAdapter.OnItemClickListener {
                            override fun onClicked(s: String) {
//                          TODO("Not yet implemented")
                            }

                            override fun onToggleClicked(s: String, action: String, pos: Int) {
                                deviceList[pos].action = action
                                listener.onToggleClicked(adapterPosition, s, action, pos)
                            }

                            override fun onDeviceRemoved(pos: Int) {
                                listener.onDeviceRemoved(adapterPosition, pos)
//                          areaList[adapterPosition].deviceList.removeAt(pos)
                                //deviceAdapter.notifyDataSetChanged()
                            }

                        })
                        rvDevices.adapter = deviceAdapter
                    }

                }
            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return areaList.size
    }

    private lateinit var listener: OnItemClickListener
    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    interface OnItemClickListener {
        fun onClicked(s: String)
        fun onToggleClicked(areaPos: Int, s: String, action: String, devicePos: Int)
        fun onDeviceRemoved(areaPos: Int, devicePos: Int)
    }

}
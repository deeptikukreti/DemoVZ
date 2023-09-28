package com.example.demovz.ui.event.adapter.addDevice

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.R
import com.example.demovz.databinding.AreaItemLayoutBinding
import com.example.demovz.db.entity.AreaWithDeviceData
import com.example.demovz.db.entity.Device

class AreaAdapter(
    private var areaList: ArrayList<AreaWithDeviceData>,
    private val isEventDetail: Boolean,
    val context:Context
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
                    cardViewMain.visibility = View.VISIBLE
                    with(areaList[position]) {
                        tvAreaName.text = this.areaName
                        binding.words.text = if (this.areaName.isNotBlank()) {
                            this.areaName.first().toString()
                        } else ""
                        if(this.isExpanded)
                        {
                            rvDevices.visibility=View.VISIBLE
                            dropIcon.setImageDrawable(context.getDrawable(R.drawable.baseline_arrow_down))
                            dropIcon.rotation=180f
                        }
                        else{
                            rvDevices.visibility=View.GONE
                            dropIcon.setImageDrawable(context.getDrawable(R.drawable.baseline_arrow_down))
                            dropIcon.rotation=0f
                        }

                       deviceListAdapter(rvDevices,adapterPosition,this.deviceList)

                        dropIcon.setOnClickListener {
                            if(this.isExpanded)
                            {
                                rvDevices.visibility=View.GONE
                                listener.onExpanded(adapterPosition,false)
                            }
                            else{
                                rvDevices.visibility=View.VISIBLE
                                listener.onExpanded(adapterPosition,true)
                            }
                        }
                    }
                }

            }
        }
    }

    private fun deviceListAdapter(rvDevices:RecyclerView,adapterPosition:Int,deviceList:ArrayList<Device>){
        val deviceAdapter = DevicesListAdapter(deviceList, isEventDetail)
        deviceAdapter.setOnClickListener(object :
            DevicesListAdapter.OnItemClickListener {

            override fun onToggleClicked(s: String, action: Boolean, pos: Int) {
                deviceList[pos].action = action
                listener.onToggleClicked(adapterPosition, s, action, pos)
            }

            override fun onDeviceRemoved(pos: Int) {
                listener.onDeviceRemoved(adapterPosition, pos)
            }

        })
        rvDevices.adapter = deviceAdapter
    }


    override fun getItemCount(): Int {
        return areaList.size
    }

    private lateinit var listener: OnItemClickListener
    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    interface OnItemClickListener {
        fun onToggleClicked(areaPos: Int, s: String, action: Boolean, devicePos: Int)
        fun onDeviceRemoved(areaPos: Int, devicePos: Int)
        fun onExpanded(areaPos: Int, isExpanded: Boolean)

    }

}
package com.example.demovz.ui.event.adapter.addDevice

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.databinding.DeviceDetailItemLayoutBinding
import com.example.demovz.databinding.DeviceItemLayoutBinding
import com.example.demovz.db.entity.Device

class DevicesListAdapter(
    private var devicesList: ArrayList<Device>,
    private var isEventDetail: Boolean,
    private val isDeviceScreen: Boolean
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
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isEventDetail) {
            with(holder as DeviceDetailViewHolder) {
                binding.apply {
                    val data = devicesList[position]
                    val action = if(data.action) "ON" else "OFF"
                    txtDeviceName.text =
                         if(action == "OFF") " ${data.deviceName} : OFF" else data.deviceName

                    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            seekBarValue.text = "$progress%"
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            //"Not yet implemented"
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            //"Not yet implemented"
                        }

                    })

                    if(action=="ON")
                    {
                        txtFeatureName.visibility=View.VISIBLE
                        txtFeatureName.text=data.feature.featureName
                        if(data.feature.uiType==1)
                        {
                            groupSeekbar.visibility=View.VISIBLE
                            seekBar.progress=data.feature.defaultFeatureValue.toInt()
                            seekBarValue.text="${data.feature.defaultFeatureValue}${data.feature.unit}"
                        }else{
                            groupDropdown.visibility=View.VISIBLE
                        }
                    }else{
                        txtDeviceName.setPadding(20,20,20,20)
                        txtFeatureName.visibility=View.GONE
                        groupSeekbar.visibility=View.GONE
                        groupDropdown.visibility=View.GONE
                    }
                }
            }
        } else {
            with(holder as ViewHolder) {
                binding.apply {
                    with(devicesList[position]) {

                        txtDeviceName.text = this.deviceName
                        toggleBtn.isChecked = this.action
                        if (isDeviceScreen){
                            cancelImg.visibility = View.GONE
                        } else{
                            cancelImg.visibility = View.VISIBLE
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
                                toggleBtn.isChecked,
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
        fun onToggleClicked(s: String, action: Boolean, position: Int)
        fun onDeviceRemoved(position: Int)
    }

}
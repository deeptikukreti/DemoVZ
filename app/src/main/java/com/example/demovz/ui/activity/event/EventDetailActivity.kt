package com.example.demovz.ui.activity.event

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.demovz.adapter.addDevice.AreaAdapter
import com.example.demovz.adapter.addDevice.DevicesListAdapter
import com.example.demovz.databinding.ActivityEventDetailBinding
import com.example.demovz.db.devices.AreaWithDeviceData
import com.example.demovz.db.events.Device
import com.example.demovz.db.events.Event
import com.example.demovz.db.events.RoomDb
import com.example.demovz.util.ArrayListConverter

class EventDetailActivity : AppCompatActivity(), AreaAdapter.OnItemClickListener {

    var binding: ActivityEventDetailBinding? = null
    lateinit var areaAdapter: AreaAdapter
    var deviceList = ArrayList<Device>()
    lateinit var deviceAdapter: DevicesListAdapter

    var eventName: String = ""
    var triggerType: Int = 0 //1=time base ,2=event based
    var dateTime: String = ""
    var isRecurring: Boolean = false
    var sensorDevice=""
    var id: Int? = 0
    lateinit var data: Event
    var selectedDeviceList = ArrayList<AreaWithDeviceData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        id = intent?.extras?.getInt("ID")


        binding?.appBar?.apply {
            ivLogo.visibility = View.GONE
            backIcon.visibility = View.VISIBLE
            editImg.visibility = View.VISIBLE
            cancelImg.visibility = View.VISIBLE
            txtTitle?.text = "Event Detail"

            backIcon.setOnClickListener {
                onBackPressed()
            }

            editImg.setOnClickListener {
                startActivity(
                    Intent(
                        this@EventDetailActivity,
                        EditEventActivity::class.java
                    ).putExtra("ID", id)
                )
            }

            cancelImg.setOnClickListener {
                RoomDb.getInstance(
                    applicationContext
                ).eventDao().delete(event = data)
                onBackPressed()
                finish()
            }
        }
    }

    private fun setData() {
        data = RoomDb.getInstance(applicationContext).eventDao().getEvent(id!!)
        if (data != null) {
            binding?.apply {
                eventName = data.eventName
                triggerType = data.triggerType
                dateTime = data.dateTime
                isRecurring = data.isRecurring
                sensorDevice=data.sensorDevice
                txtEvntName.text = "Event/Scene Name : " + data.eventName
                if (data.triggerType == 1) {
                    txtTriggerType.text = "Trigger Type : Time Based"
                    grpSelectDateTime.visibility = View.VISIBLE
                    tvDateTime.text = data.dateTime
                    if (data.isRecurring)
                        txtRecurring.text = "Event Type : Recurring"
                    else
                        txtRecurring.text = "Event Type : One Time"
                } else {
                    txtTriggerType.text = "Trigger Type : Event Based"
                    txtSensorDevice.visibility=View.VISIBLE
                    txtSensorDevice.text="Sensor Device : ${data.sensorDevice}"
                }

                selectedDeviceList =
                    ArrayListConverter().toStringArrayListAreaWithDevice(data.deviceList)
                areaAdapter = AreaAdapter(selectedDeviceList, true, this@EventDetailActivity).apply { this.setOnClickListener(this@EventDetailActivity) }
                binding?.rvGrp?.adapter = areaAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    override fun onClicked(s: String) {
       // TODO("Not yet implemented")
    }

    override fun onToggleClicked(areaPos: Int, s: String, action: Boolean, devicePos: Int) {
        //TODO("Not yet implemented")
    }

    override fun onDeviceRemoved(areaPos: Int, devicePos: Int) {
       // TODO("Not yet implemented")
    }

    override fun onExpanded(areaPos: Int, isExpanded: Boolean) {
        selectedDeviceList[areaPos].isExpanded = isExpanded
        areaAdapter.notifyDataSetChanged()
    }


}
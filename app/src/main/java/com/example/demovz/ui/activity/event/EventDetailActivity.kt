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

class EventDetailActivity : AppCompatActivity(), DevicesListAdapter.OnItemClickListener{

    var binding: ActivityEventDetailBinding? = null
    lateinit var areaAdapter: AreaAdapter
    var deviceList = ArrayList<Device>()
    lateinit var deviceAdapter: DevicesListAdapter

    var eventName: String = ""
    var triggerType: Int = 0 //1=time base ,2=event based
    var dateTime: String = ""
    var isRecurring: Boolean = false
    var id: Int? = 0
    lateinit var data: Event
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        id = intent?.extras?.getInt("ID")

        binding?.apply {

            txtCreateEvent.setOnClickListener {
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
                txtEvntName.text = "Event/Scene Name : " + data.eventName
                if (data.triggerType == 1) {
                    txtTriggerType.text = "Trigger Type : Time Based"
                    grpSelectDateTime.visibility = View.VISIBLE
                    tvDateTime.text = data.dateTime
                    cbRecurring.isChecked = data.isRecurring
                } else {
                    txtTriggerType.text = "Trigger Type : Event Based"
                }
                 var selectedDeviceList = ArrayList<AreaWithDeviceData>()
                selectedDeviceList = ArrayListConverter().toStringArrayListAreaWithDevice(data.deviceList)
                areaAdapter = AreaAdapter(selectedDeviceList, true)
                binding?.rvGrp?.adapter = areaAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    override fun onClicked(s: String) {
        //"Not yet implemented"
    }

    override fun onToggleClicked(s: String, action: String, position: Int) {
        //"Not yet implemented"
    }

    override fun onDeviceRemoved(position: Int) {
        //"Not yet implemented"
    }
}
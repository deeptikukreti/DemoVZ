package com.example.demovz.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.demovz.R
import com.example.demovz.adapter.DevicesListAdapter
import com.example.demovz.databinding.ActivityEventDetailBinding
import com.example.demovz.db.Device
import com.example.demovz.db.Event
import com.example.demovz.db.RoomDb
import com.example.demovz.util.ArrayListConverter

class EventDetailActivity : AppCompatActivity() {

    var binding: ActivityEventDetailBinding? = null
    var deviceList =ArrayList<Device>()
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
        setData()
        binding?.apply {

            txtEvntName.setOnClickListener {
                onBackPressed()
            }

            editImg.setOnClickListener {
                startActivity(Intent(this@EventDetailActivity,EditEventActivity::class.java).putExtra("ID",id))
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

                deviceList = ArrayListConverter().toStringArrayList(data.deviceList)
                deviceAdapter =
                    DevicesListAdapter(deviceList,true)
                binding?.rvGrp?.adapter = deviceAdapter
            }
        }
    }
}
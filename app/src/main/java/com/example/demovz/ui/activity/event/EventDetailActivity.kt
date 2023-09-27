package com.example.demovz.ui.activity.event

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.demovz.R
import com.example.demovz.adapter.addDevice.AreaAdapter
import com.example.demovz.databinding.ActivityEventDetailBinding
import com.example.demovz.db.devices.AreaWithDeviceData
import com.example.demovz.db.events.Event
import com.example.demovz.db.events.RoomDb
import com.example.demovz.util.ArrayListConverter


class EventDetailActivity : AppCompatActivity(), AreaAdapter.OnItemClickListener {

    var binding: ActivityEventDetailBinding? = null
    private lateinit var areaAdapter: AreaAdapter

    private var eventName: String = ""
    private var triggerType: Int = 0 //1=time base ,2=event based
    private var dateTime: String = ""
    private var isRecurring: Boolean = false
    private var sensorDevice = ""
    private var eventId: Int? = 0
    private lateinit var data: Event
    private var selectedDeviceList = ArrayList<AreaWithDeviceData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        eventId = intent?.extras?.getInt("ID")


        binding?.appBar?.apply {
            setSupportActionBar(toolbar)
            ivLogo.visibility = View.GONE
            backIcon.visibility = View.VISIBLE
//            editImg.visibility = View.VISIBLE
//            cancelImg.visibility = View.VISIBLE
            txtTitle.text = getString(R.string.event_details)
            this.toolbar.title = ""

            backIcon.setOnClickListener {
                onBackPressed()
            }

            editImg.setOnClickListener {
                startActivity(
                    Intent(
                        this@EventDetailActivity,
                        EditEventActivity::class.java
                    ).putExtra("ID", eventId)
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

    @SuppressLint("SetTextI18n")
    private fun setData() {
        data = RoomDb.getInstance(applicationContext).eventDao().getEvent(eventId!!)
        if (data != null) {
            binding?.apply {
                eventName = data.eventName
                triggerType = data.triggerType
                dateTime = data.dateTime
                isRecurring = data.isRecurring
                sensorDevice = data.sensorDevice
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
                    txtTriggerType.text = "Trigger Type : Activity Based"
                    txtSensorDevice.visibility = View.VISIBLE
                    txtSensorDevice.text = "Sensor Device : ${data.sensorDevice}"
                }

                selectedDeviceList =
                    ArrayListConverter().toStringArrayListAreaWithDevice(data.deviceList)
                selectedDeviceList.forEach {
                    it.isExpanded=true
                }
                areaAdapter = AreaAdapter(
                    selectedDeviceList,
                    true,
                    this@EventDetailActivity
                ).apply { this.setOnClickListener(this@EventDetailActivity) }
                binding?.rvGrp?.adapter = areaAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    override fun onToggleClicked(areaPos: Int, s: String, action: Boolean, devicePos: Int) {
        //TODO("Not yet implemented")
    }

    override fun onDeviceRemoved(areaPos: Int, devicePos: Int) {
        // TODO("Not yet implemented")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onExpanded(areaPos: Int, isExpanded: Boolean) {
        selectedDeviceList[areaPos].isExpanded = isExpanded
        areaAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit -> {
                startActivity(
                    Intent(
                        this@EventDetailActivity,
                        EditEventActivity::class.java
                    ).putExtra("ID", eventId)
                )
                true
            }

            R.id.delete -> {
                RoomDb.getInstance(applicationContext).eventDao().delete(event = data)
                onBackPressed()
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
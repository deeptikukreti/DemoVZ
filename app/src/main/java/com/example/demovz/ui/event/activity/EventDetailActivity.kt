package com.example.demovz.ui.event.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.demovz.R
import com.example.demovz.databinding.ActivityEventDetailBinding
import com.example.demovz.db.entity.Device
import com.example.demovz.db.entity.Event
import com.example.demovz.ui.event.adapter.addDevice.DevicesListAdapter
import com.example.demovz.ui.event.viewModel.EventViewModel
import com.example.demovz.util.ArrayListConverter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventDetailActivity : AppCompatActivity(){

    var binding: ActivityEventDetailBinding? = null

    private val eventViewModel : EventViewModel by viewModels()

    private lateinit var deviceAdapter: DevicesListAdapter

    private var eventName: String = ""
    private var triggerType: Int = 0 //1=time base ,2=event based
    private var dateTime: String = ""
    private var isRecurring: Boolean = false
    private var sensorDevice = ""
    private var eventId: Int? = 0
    private lateinit var data: Event
    private var selectedDeviceList = ArrayList<Device>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        eventId = intent?.extras?.getInt("ID")
        viewInit()
    }

    private fun viewInit() {
        binding?.appBar?.apply {
            setSupportActionBar(toolbar)
            ivLogo.visibility = View.GONE
            backIcon.visibility = View.VISIBLE
            txtTitle.text = getString(R.string.event_details)
            this.toolbar.title = ""

            backIcon.setOnClickListener {
                onBackPressed()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        eventViewModel.getEventById(eventId!!).observe(this) {
            it?.let {
                data = it
                binding?.apply {
                    eventName = data.eventName
                    triggerType = data.triggerType
                    dateTime = data.dateTime
                    isRecurring = data.isRecurring
                    sensorDevice = data.sensorDevice
                    txtEvntName.text = "Event Name: " + data.eventName
                    if (data.triggerType == 1) {
                        txtTriggerType.text = "Trigger Type : Time Based"
                        grpSelectDateTime.visibility = View.VISIBLE
                        tvDateTime.text = data.dateTime
                        if (data.isRecurring)
                            txtRecurring.text = "Occurrence type : Recurring"
                        else
                            txtRecurring.text = "Occurrence type : One Time"
                    } else {
                        txtTriggerType.text = "Trigger Type : Activity Based"
                        txtSensorDevice.visibility = View.VISIBLE
                        txtSensorDevice.text = "Sensor / Device : ${data.sensorDevice}"
                    }
                    txtArea.text = "Area : ${data.areaName}"
                    selectedDeviceList =
                        ArrayListConverter().toStringArrayList(data.selectDeviceList)
                    deviceAdapter = DevicesListAdapter(selectedDeviceList, true, false)
                    binding?.rvGrp?.adapter = deviceAdapter
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
                eventViewModel.deleteEvent(data)
                eventViewModel.deleteEventConfirmationDialog(this@EventDetailActivity)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
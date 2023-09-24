package com.example.demovz.ui.activity.event

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.R
import com.example.demovz.adapter.addDevice.AreaAdapter
import com.example.demovz.adapter.addDevice.SelectDevicesListAdapter
import com.example.demovz.databinding.ActivityCreateEventBinding
import com.example.demovz.db.devices.Area
import com.example.demovz.db.devices.AreaWithDeviceData
import com.example.demovz.db.devices.DevicesRoomDb
import com.example.demovz.db.devices.SelectDeviceData
import com.example.demovz.db.events.Device
import com.example.demovz.db.events.Event
import com.example.demovz.db.events.RoomDb
import com.example.demovz.util.ArrayListConverter
import java.util.Calendar

class CreateEventActivity : AppCompatActivity(), AreaAdapter.OnItemClickListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var binding: ActivityCreateEventBinding? = null
    lateinit var areaAdapter: AreaAdapter

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    var eventName: String = ""
    var triggerType: Int = 0 //1=time base ,2=event based
    var dateTime: String = ""
    var isRecurring: Boolean = false

    var areaDeviceList = ArrayList<Area>()
    private var selectedDeviceList = ArrayList<AreaWithDeviceData>()
    private var selectDeviceList = ArrayList<SelectDeviceData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        DevicesRoomDb.getInstance(applicationContext).areaDeviceDao().getAllAreaWithDevices()
            .also { areaDeviceList.addAll(it) }
        areaDeviceList.forEach { area ->
            val deviceList = ArrayListConverter().toStringArrayList(area.deviceList)
            selectedDeviceList.add(
                AreaWithDeviceData(
                    area.areaId,
                    area.areaName,
                    ArrayList<Device>()
                )
            )
            deviceList.forEach {
                selectDeviceList.add(SelectDeviceData(area.areaId, area.areaName, it))

            }
        }
        viewInitialization()
    }

    private fun viewInitialization() {
        areaAdapter = AreaAdapter(selectedDeviceList,false).apply { this.setOnClickListener(this@CreateEventActivity) }
        binding?.rvGrp?.adapter = areaAdapter
        binding?.apply {

            rgTriggerType.setOnCheckedChangeListener { radioGroup, i ->
                when (i) {
                    R.id.rb_time_based -> {
                        triggerType = 1
                        grpSelectDateTime.visibility = View.VISIBLE
                    }

                    R.id.rb_event_based -> {
                        triggerType = 2
                    }
                }
            }

            tvDateTime.setOnClickListener {
                val calendar: Calendar = Calendar.getInstance()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(
                        this@CreateEventActivity,
                        this@CreateEventActivity,
                        year,
                        month,
                        day
                    )
                datePickerDialog.show()
                // grpAddDevice.visibility=View.VISIBLE
            }

            cbRecurring.setOnCheckedChangeListener { buttonView, isChecked ->
                isRecurring = isChecked
            }
            txtAddDevices.setOnClickListener {
                createAlertDialog()
            }

            btnSaveEvent.setOnClickListener {
                if (edtEventName.text.toString().isEmpty()) {
                    Toast.makeText(
                        this@CreateEventActivity,
                        "Please enter event name",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    eventName = edtEventName.text.toString()
                    saveEventDetails()
                }
            }
        }
    }

    private fun saveEventDetails() {
        val eventObj = Event(
            eventName = eventName,
            triggerType = triggerType,
            dateTime = dateTime,
            isRecurring = isRecurring,
            deviceList = ArrayListConverter().fromStringArrayListAreaWithDevice(selectedDeviceList)
        )
        RoomDb.getInstance(applicationContext).eventDao().insert(eventObj)
        onBackPressed();
        finish()
    }

    override fun onClicked(s: String) {
        TODO("Not yet implemented")
    }

    override fun onToggleClicked(areaPos: Int, s: String, action: String, devicePos: Int) {
        selectedDeviceList[areaPos].deviceList[devicePos].action = action
    }

    override fun onDeviceRemoved(areaPos: Int, position: Int) {
        selectedDeviceList[areaPos].deviceList.removeAt(position)
        areaAdapter.notifyDataSetChanged()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog =
            TimePickerDialog(this@CreateEventActivity, this@CreateEventActivity, hour, minute, true)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        binding?.tvDateTime?.text = "$myDay-$myMonth-$myYear, $myHour:$myMinute"
        dateTime = "$myDay-$myMonth-$myYear, $myHour:$myMinute"
        binding?.grpAddDevice?.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    private fun createAlertDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.add_device_layout, null)
        val rvSelectDevice = view.findViewById<RecyclerView>(R.id.rvSelectDevice)
        val save = view.findViewById<Button>(R.id.btn_save)
        val cancel = view.findViewById<Button>(R.id.btn_cancel)
        builder.setView(view)
        var _selectedDeviceList = ArrayList<SelectDeviceData>()
        val deviceAdapter = SelectDevicesListAdapter()
        deviceAdapter.setOnClickListener(object :
            SelectDevicesListAdapter.OnItemClickListener {
            override fun onClicked(i: SelectDeviceData, isChecked: Boolean, position: Int) {
                if (isChecked) {
                    selectDeviceList.get(position).device.isSelected = true
                    _selectedDeviceList.add(i)
                    selectedDeviceList.forEach {
                        if (it.areaId == i.areaId)
                            it.deviceList.add(i.device)
                    }
                } else {
                    selectDeviceList.get(position).device.isSelected = false
                    _selectedDeviceList.remove(i)
                    selectedDeviceList.forEach {
                        if (it.areaId == i.areaId)
                            it.deviceList.remove(i.device)
                    }
                }
            }

        })
        deviceAdapter.addList(selectDeviceList)
        rvSelectDevice.adapter = deviceAdapter
        save.setOnClickListener {
            var isDeviceSelected=false
            for(item in selectedDeviceList) {
               if(item.deviceList.size>0)
               {
                   isDeviceSelected=true
                   break
               }
            }
            if(isDeviceSelected) {
                areaAdapter.notifyDataSetChanged()
                binding?.btnSaveEvent?.visibility=View.VISIBLE
                builder.dismiss()
            }else{
                Toast.makeText(this,"Please select any device",Toast.LENGTH_LONG).show()
            }
        }
        cancel.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

}
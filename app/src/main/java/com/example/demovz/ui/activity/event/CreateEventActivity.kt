package com.example.demovz.ui.activity.event

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
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
import com.example.demovz.db.events.Event
import com.example.demovz.db.events.RoomDb
import com.example.demovz.util.ArrayListConverter
import com.example.demovz.util.CommonUtils
import java.util.Calendar


class CreateEventActivity : AppCompatActivity(), AreaAdapter.OnItemClickListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var binding: ActivityCreateEventBinding? = null
    private lateinit var areaAdapter: AreaAdapter

    private var hour: Int = 0
    private var minute: Int = 0
    private var myDay = 0
    private var myMonth: Int = 0
    private var myYear: Int = 0
    private var myHour: Int = 0
    private var myMinute: Int = 0
    private var dateTimeSelectionType: Int = 0 //1=start date time, 2=end date time

    private var eventName: String = ""
    private var triggerType: Int = 0 //1=time base ,2=event based
    private var dateTime: String = ""
    private var endDateTime: String = ""
    private var isRecurring: Boolean = false
    var sensorDevice = ""

    private var areaDeviceList = ArrayList<Area>()
    private var selectedDeviceList = ArrayList<AreaWithDeviceData>()
    private var selectedDeviceListForUI = ArrayList<AreaWithDeviceData>()
    private var selectDeviceList = ArrayList<SelectDeviceData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        getDeviceData()
        setSpinner()
        viewInitialization()
    }

    private fun getDeviceData() {
        DevicesRoomDb.getInstance(applicationContext).areaDeviceDao().getAllAreaWithDevices()
            .also { areaDeviceList.addAll(it) }
        areaDeviceList.forEach { area ->
            val deviceList = ArrayListConverter().toStringArrayList(area.deviceList)
            selectedDeviceList.add(
                AreaWithDeviceData(
                    area.areaId,
                    area.areaName,
                    ArrayList()
                )
            )
            deviceList.forEach {
                selectDeviceList.add(SelectDeviceData(area.areaId, area.areaName, it))

            }
        }
    }

    private fun setSpinner() {

        val list = arrayListOf(
            "Please Select Sensor Device: ",
            "Main Door Sensor",
            "Bedroom Door Sensor",
            "Kitchen Door sensor",
            "Stairs Sensor"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        binding?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // You can define your actions as you want
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                if (position == 0) {
                    (parent?.getChildAt(position) as TextView).setTextColor(getColor(R.color.colorGrey))
                } else {
                    sensorDevice = list[position]
                    binding?.grpAddDevice?.visibility = View.VISIBLE
                }
            }
        }
        binding?.spinner?.adapter = adapter

    }

    private fun viewInitialization() {
        binding?.appBar?.apply {
            ivLogo.visibility = View.GONE
            backIcon.visibility = View.VISIBLE
            txtTitle.text = getString(R.string.create_event)

            backIcon.setOnClickListener {
                onBackPressed()
            }
        }
        areaAdapter = AreaAdapter(
            selectedDeviceListForUI,
            false,
            this
        ).apply { this.setOnClickListener(this@CreateEventActivity) }
        binding?.rvGrp?.adapter = areaAdapter
        binding?.apply {

            rgTriggerType.setOnCheckedChangeListener { _, i ->
                when (i) {
                    R.id.rb_time_based -> {
                        sensorDevice = ""
                        triggerType = 1
                        binding?.spinner?.setSelection(0)
                        grpSelectDateTime.visibility = View.VISIBLE
                        grpSelectEventType.visibility = View.GONE
                    }

                    R.id.rb_event_based -> {
                        triggerType = 2
                        isRecurring = false
                        dateTime = ""
                        endDateTime = ""
                        tvDateTime.text = ""
                        tvToDateTime.text = ""
                        grpSelectDateTime.visibility = View.GONE
                        grpSelectEventType.visibility = View.VISIBLE
                    }
                }
            }

            tvDateTime.setOnClickListener {
                val calendar: Calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(
                        this@CreateEventActivity,
                        this@CreateEventActivity,
                        year,
                        month,
                        day
                    )
                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
                dateTimeSelectionType = 1
            }

            tvToDateTime.setOnClickListener {
                val calendar: Calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(
                        this@CreateEventActivity,
                        this@CreateEventActivity,
                        year,
                        month,
                        day
                    )
                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
                dateTimeSelectionType = 2
            }

            cbRecurring.setOnCheckedChangeListener { _, isChecked ->
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
                } else if (triggerType == 1 && dateTime.isEmpty()) {
                    Toast.makeText(
                        this@CreateEventActivity,
                        "Please select date and time",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (triggerType == 1 && endDateTime.isEmpty()) {
                    Toast.makeText(
                        this@CreateEventActivity,
                        "Please select end date and time",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (triggerType == 1 && !CommonUtils.isDateAfter(dateTime, endDateTime)) {
                    Toast.makeText(
                        this@CreateEventActivity,
                        "Please select Correct Date time, end date and time can't before to start data",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (triggerType == 2 && sensorDevice.isEmpty()) {
                    Toast.makeText(
                        this@CreateEventActivity,
                        "Please select sensor device type",
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
            dateTime = "$dateTime to $endDateTime",
            isRecurring = isRecurring,
            sensorDevice = sensorDevice,
            deviceList = ArrayListConverter().fromStringArrayListAreaWithDevice(
                selectedDeviceListForUI
            )
        )
        RoomDb.getInstance(applicationContext).eventDao().insert(eventObj)
        onBackPressed()
        finish()
    }

    override fun onClicked(s: String) {
        //"Not yet implemented"
    }

    override fun onToggleClicked(areaPos: Int, s: String, action: Boolean, devicePos: Int) {
        selectedDeviceListForUI[areaPos].deviceList[devicePos].action = action
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDeviceRemoved(areaPos: Int, devicePos: Int) {
        selectedDeviceListForUI[areaPos].deviceList.removeAt(devicePos)
        areaAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onExpanded(areaPos: Int, isExpanded: Boolean) {
        selectedDeviceListForUI[areaPos].isExpanded = isExpanded
        areaAdapter.notifyDataSetChanged()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month + 1
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog =
            TimePickerDialog(this@CreateEventActivity, this@CreateEventActivity, hour, minute, true)
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        when (dateTimeSelectionType) {
            1 -> {
                binding?.tvDateTime?.text = "$myDay-$myMonth-$myYear, $myHour:$myMinute"
                dateTime = "$myDay-$myMonth-$myYear, $myHour:$myMinute"
            }

            2 -> {
                binding?.tvToDateTime?.text = "$myDay-$myMonth-$myYear, $myHour:$myMinute"
                endDateTime = "$myDay-$myMonth-$myYear, $myHour:$myMinute"
                binding?.grpAddDevice?.visibility = View.VISIBLE
            }
        }
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
        val deviceAdapter = SelectDevicesListAdapter()
        deviceAdapter.setOnClickListener(object :
            SelectDevicesListAdapter.OnItemClickListener {
            override fun onClicked(i: SelectDeviceData, isChecked: Boolean, position: Int) {
                if (isChecked) {
                    selectDeviceList[position].device.isSelected = true
                    selectedDeviceList.forEach {
                        if (it.areaId == i.areaId)
                            it.deviceList.add(i.device)
                    }
                } else {
                    selectDeviceList[position].device.isSelected = false
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
            var isDeviceSelected = false
            for (item in selectedDeviceList) {
                if (item.deviceList.size > 0) {
                    isDeviceSelected = true
                    break
                }
            }
            if (isDeviceSelected) {
                selectedDeviceListForUI.clear()
                selectedDeviceList.forEach {
                    if (it.deviceList.size > 0)
                        selectedDeviceListForUI.add(it)
                }
                areaAdapter.notifyDataSetChanged()
                binding?.btnSaveEvent?.visibility = View.VISIBLE
                builder.dismiss()
            } else {
                Toast.makeText(this, "Please select any device", Toast.LENGTH_LONG).show()
            }
        }
        cancel.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

}
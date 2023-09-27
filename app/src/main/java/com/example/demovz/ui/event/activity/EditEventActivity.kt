package com.example.demovz.ui.event.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
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
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.R
import com.example.demovz.ui.event.adapter.addDevice.AreaAdapter
import com.example.demovz.ui.event.adapter.addDevice.AddDeviceAdapter
import com.example.demovz.databinding.ActivityEditEventBinding
import com.example.demovz.db.devices.AreaWithDeviceData
import com.example.demovz.db.devices.DevicesRoomDb
import com.example.demovz.db.events.Device
import com.example.demovz.db.events.Event
import com.example.demovz.db.events.RoomDb
import com.example.demovz.util.ArrayListConverter
import com.example.demovz.util.CommonUtils
import java.util.Calendar

class EditEventActivity : AppCompatActivity(), AreaAdapter.OnItemClickListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var binding: ActivityEditEventBinding? = null

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
    var id: Int? = 0

    private var selectDeviceList = ArrayList<AreaWithDeviceData>()
    private var selectedDeviceListForUI = ArrayList<AreaWithDeviceData>()

    val list = arrayListOf(
        "Please Select Sensor Device: ",
        "Main Door Sensor",
        "Bedroom Door Sensor",
        "Kitchen Door sensor",
        "Stairs Sensor"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        id = intent?.extras?.getInt("ID")
        getDeviceData()
        setSpinner()
        setData()
        viewInitialization()
    }

    private fun getDeviceData() {
        DevicesRoomDb.getInstance(applicationContext).areaDeviceDao().getAllAreaWithDevices()
            .onEach { area ->
                val deviceList = ArrayListConverter().toStringArrayList(area.deviceList)
                selectDeviceList.add(
                    AreaWithDeviceData(
                        area.areaId,
                        area.areaName,
                        deviceList,
                        false
                    )
                )
            }
    }

    private fun setSpinner() {
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

    private fun setData() {
        val data = RoomDb.getInstance(applicationContext).eventDao().getEvent(id!!)
        data?.let {
            binding?.apply {
                eventName = data.eventName
                triggerType = data.triggerType
                val bothDate = data.dateTime.split("to")
                if (bothDate.isNotEmpty()) {
                    dateTime = bothDate[0]
                }
                if (bothDate.size > 1) {
                    endDateTime = bothDate[1]
                }
                isRecurring = data.isRecurring
                sensorDevice = data.sensorDevice
                edtEventName.setText(data.eventName)
                if (data.triggerType == 1) {
                    rbTimeBased.isChecked = true
                    grpSelectDateTime.visibility = View.VISIBLE
                    grpAddDevice.visibility = View.VISIBLE
                    tvDateTime.text = dateTime
                    tvToDateTime.text = endDateTime
                    cbRecurring.isChecked = data.isRecurring
                } else {
                    rbEventBased.isChecked = true
                    grpSelectEventType.visibility = View.VISIBLE
                    binding?.spinner?.setSelection(list.indexOf(sensorDevice))
                }

                btnSaveEvent.visibility = View.VISIBLE

                selectedDeviceListForUI =
                    ArrayListConverter().toStringArrayListAreaWithDevice(data.deviceList)
                selectDeviceList =
                    ArrayListConverter().toStringArrayListAreaWithDevice(data.selectDeviceList)

                selectedDeviceListForUI.forEach {
                    it.isExpanded = true
                }

                areaAdapter =
                    AreaAdapter(
                        selectedDeviceListForUI,
                        false,
                        this@EditEventActivity
                    ).apply { setOnClickListener(this@EditEventActivity) }
                binding?.rvGrp?.adapter = areaAdapter
            }
        }
    }

    private fun viewInitialization() {
        binding?.appBar?.apply {
            ivLogo.visibility = View.GONE
            backIcon.visibility = View.VISIBLE
            txtTitle.text = getString(R.string.edit_event)
            backIcon.visibility = View.VISIBLE
            editImg.visibility = View.GONE
            cancelImg.visibility = View.GONE
            backIcon.setOnClickListener {
                onBackPressed()
            }
        }
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
                        this@EditEventActivity,
                        this@EditEventActivity,
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
                        this@EditEventActivity,
                        this@EditEventActivity,
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
                        this@EditEventActivity,
                        "Please enter event name",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (triggerType == 1 && dateTime.isEmpty()) {
                    Toast.makeText(
                        this@EditEventActivity,
                        "Please select date and time",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (triggerType == 1 && endDateTime.isEmpty()) {
                    Toast.makeText(
                        this@EditEventActivity,
                        "Please select end date and time",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (triggerType == 1 && !CommonUtils.isDateAfter(dateTime, endDateTime)) {
                    Toast.makeText(
                        this@EditEventActivity,
                        "Please select Correct Date time, end date and time can't before to start data",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (triggerType == 2 && sensorDevice.isEmpty()) {
                    Toast.makeText(
                        this@EditEventActivity,
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
            id = id,
            eventName = eventName,
            triggerType = triggerType,
            dateTime = "$dateTime to $endDateTime",
            isRecurring = isRecurring,
            sensorDevice = sensorDevice,
            deviceList = ArrayListConverter().fromStringArrayListAreaWithDevice(
                selectedDeviceListForUI
            ),
            selectDeviceList = ArrayListConverter().fromStringArrayListAreaWithDevice(
                selectDeviceList
            )
        )
        RoomDb.getInstance(applicationContext).eventDao().update(eventObj)
        onBackPressed()
        finish()
    }

    override fun onToggleClicked(areaPos: Int, s: String, action: Boolean, devicePos: Int) {
        selectedDeviceListForUI[areaPos].deviceList[devicePos].action = action
    }

    override fun onDeviceRemoved(areaPos: Int, devicePos: Int) {
        val deviceName = selectedDeviceListForUI[areaPos].deviceList[devicePos].deviceName
        selectDeviceList.single { it.areaId == selectedDeviceListForUI[areaPos].areaId }
            .deviceList.single { it.deviceName == deviceName }.isSelected = false

        selectedDeviceListForUI[areaPos].deviceList.removeAt(devicePos)

        if (selectedDeviceListForUI[areaPos].deviceList.isEmpty())
            selectedDeviceListForUI.removeAt(areaPos)

        areaAdapter.notifyDataSetChanged()
    }

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
            TimePickerDialog(this@EditEventActivity, this@EditEventActivity, hour, minute, true)
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
        selectDeviceList.forEach { it.isExpanded = false }
        val addDeviceAdapter = AddDeviceAdapter(selectDeviceList, this)
        addDeviceAdapter.setOnClickListener(object :
            AddDeviceAdapter.OnItemClickListener {

            override fun onClicked(areaPos: Int, i: Device, isChecked: Boolean, devicePos: Int) {
                selectDeviceList[areaPos].deviceList[devicePos].isSelected = isChecked
            }

            override fun onExpanded(areaPos: Int, isExpanded: Boolean) {
                selectDeviceList[areaPos].isExpanded = isExpanded
                addDeviceAdapter.notifyDataSetChanged()
            }

        })
        rvSelectDevice.adapter = addDeviceAdapter
        save.setOnClickListener {
            var isDeviceSelected = false
            for (item in selectDeviceList) {
                if (item.deviceList.size > 0) {
                    isDeviceSelected = true
                    break
                }
            }
            if (isDeviceSelected) {
                selectedDeviceListForUI.clear()
                selectDeviceList.forEach {
                    val list = it.deviceList.filter { it.isSelected }
                    if (list.isNotEmpty())
                        selectedDeviceListForUI.add(
                            AreaWithDeviceData(
                                it.areaId,
                                it.areaName,
                                list as ArrayList<Device>
                            )
                        )
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
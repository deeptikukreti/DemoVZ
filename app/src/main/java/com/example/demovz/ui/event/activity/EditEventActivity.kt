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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.demovz.R
import com.example.demovz.ui.event.adapter.addDevice.AreaAdapter
import com.example.demovz.ui.event.adapter.addDevice.AddDeviceAdapter
import com.example.demovz.databinding.ActivityEditEventBinding
import com.example.demovz.db.entity.AreaWithDeviceData
import com.example.demovz.db.entity.Device
import com.example.demovz.db.entity.Event
import com.example.demovz.ui.event.adapter.addDevice.DevicesListAdapter
import com.example.demovz.ui.event.adapter.addDevice.SelectDevicesListAdapter
import com.example.demovz.ui.event.viewModel.EventViewModel
import com.example.demovz.ui.home.viewmodel.DeviceViewModel
import com.example.demovz.util.ArrayListConverter
import com.example.demovz.util.CommonUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class EditEventActivity : AppCompatActivity(), DevicesListAdapter.OnItemClickListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var binding: ActivityEditEventBinding? = null
    private val eventViewModel : EventViewModel by viewModels()
    private val deviceViewModel : DeviceViewModel by viewModels()
    private lateinit var devicesListAdapter: DevicesListAdapter

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
    var eventId: Int? = 0
    var areaId=-1
    var currentAreaId=-1
    var areaName=""

    private var selectDeviceList = ArrayList<AreaWithDeviceData>()
    private var deviceListByAreaId = ArrayList<Device>()
    private var areaList = ArrayList<String>()
    private var selectedDeviceListByAreaId = ArrayList<Device>()

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
        eventId = intent?.extras?.getInt("ID")
        getDeviceData()
        setSpinner()
        setAreaSpinner()
        setData()
        viewInitialization()
    }

    private fun getDeviceData() {
        areaList.add("Select Area :")
        deviceViewModel.getDevices().observe(this) {
            it.forEach { area ->
                val deviceList = ArrayListConverter().toStringArrayList(area.deviceList)
                areaList.add(area.areaName)
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
    private fun setAreaSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, areaList)
        binding?.areaSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // You can define your actions as you want
            }

            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position == 0) {
                    areaId=-1
                    areaName=""
                    binding?.grpAddDevice?.visibility=View.GONE
                    (parent?.getChildAt(position) as TextView).setTextColor(getColor(R.color.colorGrey))
                } else {
                    areaName=selectDeviceList[position-1].areaName
                    areaId=selectDeviceList[position-1].areaId
                    if(currentAreaId!=areaId) {
                        selectedDeviceListByAreaId.clear()
                        devicesListAdapter.notifyDataSetChanged()
                    }
                    binding?.grpAddDevice?.visibility=View.VISIBLE
                }
            }
        }
        binding?.areaSpinner?.adapter = adapter

    }
    private fun setData() {
        eventViewModel.getEventById(eventId!!).observe(this, Observer {data->
            data?.let {
                binding?.apply {
                    eventName = data.eventName
                    triggerType = data.triggerType
                    areaId=data.areaId
                    currentAreaId=data.areaId
                    areaName=data.areaName
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
                        spinner.setSelection(list.indexOf(sensorDevice))
                    }
                    grpSelectArea.visibility=View.VISIBLE
                    areaSpinner.setSelection(areaList.indexOf(areaName))
                    btnSaveEvent.visibility = View.VISIBLE

                    selectedDeviceListByAreaId =
                        ArrayListConverter().toStringArrayList(data.selectDeviceList)

                    deviceListByAreaId =
                        ArrayListConverter().toStringArrayList(data.deviceList)

                    devicesListAdapter =
                        DevicesListAdapter(
                            selectedDeviceListByAreaId,
                            false
                        ).apply { setOnClickListener(this@EditEventActivity) }
                   rvGrp.adapter = devicesListAdapter
                }
            }
        })
    }
    private fun viewInitialization() {
        binding?.appBar?.apply {
            ivLogo.visibility = View.GONE
            backIcon.visibility = View.VISIBLE
            txtTitle.text = getString(R.string.edit_event)

            backIcon.setOnClickListener {
                onBackPressed()
            }
        }

        binding?.apply {

            rgTriggerType.setOnCheckedChangeListener { _, i ->
                when (i) {
                    R.id.rb_time_based -> {
                        sensorDevice=""
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
            id = eventId,
            eventName = eventName,
            triggerType = triggerType,
            dateTime = "$dateTime to $endDateTime",
            isRecurring = isRecurring,
            sensorDevice = sensorDevice,
            areaId=areaId,
            areaName =areaName ,
            deviceList = ArrayListConverter().fromStringArrayList(deviceListByAreaId),
            selectDeviceList = ArrayListConverter().fromStringArrayList(selectedDeviceListByAreaId)
        )
        eventViewModel.updateEvent(eventObj)
//        RoomDb.getInstance(applicationContext).eventDao().update(eventObj)
        onBackPressed()
        finish()
    }
    override fun onToggleClicked(s: String, action: Boolean, position: Int) {
        selectedDeviceListByAreaId[position].action = action
    }
    override fun onDeviceRemoved(position: Int) {
        deviceListByAreaId.single { it.deviceName == selectedDeviceListByAreaId[position].deviceName }.isSelected=false
        selectedDeviceListByAreaId.removeAt(position)
        devicesListAdapter.notifyDataSetChanged()
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
        if(currentAreaId!=areaId) {
            deviceListByAreaId.clear()
            for (i in selectDeviceList) {
                if (i.areaId == areaId) {
                    currentAreaId=areaId
                    deviceListByAreaId.addAll(i.deviceList)
                    break
                }
            }
        }else{
            if(selectedDeviceListByAreaId.size>0) {
                selectedDeviceListByAreaId.forEach { it1 ->
                    deviceListByAreaId.single { it.deviceName == it1.deviceName }.isSelected = true
                }
            }else{
                deviceListByAreaId.forEach { it.isSelected=false }
            }
        }
        val addDeviceAdapter= SelectDevicesListAdapter()
        addDeviceAdapter.setOnClickListener(object : SelectDevicesListAdapter.OnItemClickListener{
            override fun onClicked(i: Device, isChecked: Boolean, position: Int) {
                deviceListByAreaId[position].isSelected=isChecked
            }
        })
        addDeviceAdapter.addList(deviceListByAreaId)
        rvSelectDevice.adapter = addDeviceAdapter
        save.setOnClickListener {
            var isDeviceSelected = false
            for(item in deviceListByAreaId){
                if(item.isSelected)
                { isDeviceSelected=true
                    break
                }
            }

            if (isDeviceSelected) {
                selectedDeviceListByAreaId.clear()
                deviceListByAreaId.forEach {
                    if(it.isSelected)
                        selectedDeviceListByAreaId.add(it)
                }
                devicesListAdapter.notifyDataSetChanged()
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
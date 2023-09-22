package com.example.demovz.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.demovz.R
import com.example.demovz.adapter.DevicesListAdapter
import com.example.demovz.databinding.ActivityEditEventBinding
import com.example.demovz.db.Device
import com.example.demovz.db.Event
import com.example.demovz.db.RoomDb
import com.example.demovz.util.ArrayListConverter
import java.util.Calendar

class EditEventActivity : AppCompatActivity(), DevicesListAdapter.OnItemClickListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var binding : ActivityEditEventBinding?=null
    var deviceList =ArrayList<Device>()
    lateinit var deviceAdapter: DevicesListAdapter

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

    var eventName: String=""
    var triggerType: Int=0 //1=time base ,2=event based
    var dateTime: String=""
    var isRecurring:Boolean=false
    var id :Int?=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        id = intent?.extras?.getInt("ID")
        setData()
        viewInitialization()
    }

    private fun setData(){

        val data = RoomDb.getInstance(applicationContext).eventDao().getEvent(id!!)
        if(data!=null) {
            binding?.apply {
                eventName=data.eventName
                triggerType=data.triggerType
                dateTime=data.dateTime
                isRecurring=data.isRecurring
                edtEventName.setText(data.eventName)
                if (data.triggerType == 1) {
                    rbTimeBased.isChecked = true
                    grpSelectDateTime.visibility=View.VISIBLE
                    grpAddDevice.visibility=View.VISIBLE
                    tvDateTime.text = data.dateTime
                    cbRecurring.isChecked = data.isRecurring
                } else
                    rbEventBased.isChecked = true

                btnSaveEvent.visibility=View.VISIBLE
                deviceList = ArrayListConverter().toStringArrayList(data.deviceList)
                deviceAdapter =
                    DevicesListAdapter(deviceList).apply { setOnClickListener(this@EditEventActivity) }
                binding?.rvGrp?.adapter = deviceAdapter
            }
        }
    }

    private fun viewInitialization() {
        binding?.apply {

            rgTriggerType.setOnCheckedChangeListener { radioGroup, i ->
                when(i){
                    R.id.rb_time_based ->{
                        triggerType=1
                        grpSelectDateTime.visibility= View.VISIBLE}
                    R.id.rb_event_based ->{
                        triggerType=2
                    }
                }
            }

            tvDateTime.setOnClickListener {
                val calendar: Calendar = Calendar.getInstance()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(this@EditEventActivity, this@EditEventActivity, year, month,day)
                datePickerDialog.show()
                // grpAddDevice.visibility=View.VISIBLE
            }

            cbRecurring.setOnCheckedChangeListener { buttonView, isChecked ->
                isRecurring=isChecked
            }
            txtAddDevices.setOnClickListener {
                createAlertDialog()
            }

            btnSaveEvent.setOnClickListener {
                if(edtEventName.text.toString().isEmpty())
                {
                    Toast.makeText(this@EditEventActivity,"Please enter event name", Toast.LENGTH_LONG).show()
                }
                else{
                    eventName=edtEventName.text.toString()
                    saveEventDetails()
                }
            }
        }
    }

    private fun saveEventDetails(){
        val eventObj = Event(id=id ,eventName = eventName,triggerType=triggerType,dateTime=dateTime,isRecurring=isRecurring,deviceList= ArrayListConverter().fromStringArrayList(deviceList))
        RoomDb.getInstance(applicationContext).eventDao().update(eventObj)
        onBackPressed();
        finish()
    }
    private fun createAlertDialog(){
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.add_device_layout,null)
        val  device_name = view.findViewById<EditText>(R.id.edt_device)
        val  save = view.findViewById<Button>(R.id.btn_save)
        val  cancel = view.findViewById<Button>(R.id.btn_cancel)
        builder.setView(view)
        save.setOnClickListener {
            deviceList.add(Device(device_name.text.toString(),"Off"))
            deviceAdapter.notifyDataSetChanged()
            if(deviceList.size>0)
                binding?.btnSaveEvent?.visibility= View.VISIBLE
            builder.dismiss()
        }
        cancel.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    override fun onClicked(s: String) {
        TODO("Not yet implemented")
    }

    override fun onToggleClicked(s: String, action: String,position: Int) {
        deviceList[position].action=action
    }

    override fun onDeviceRemoved(position: Int) {
        deviceList.removeAt(position)
        deviceAdapter.notifyDataSetChanged()
        if(deviceList.size>0)
            binding?.btnSaveEvent?.visibility= View.VISIBLE
        else
            binding?.btnSaveEvent?.visibility= View.GONE
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this@EditEventActivity, this@EditEventActivity, hour, minute,true)
        timePickerDialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        binding?.tvDateTime?.text = "$myDay-$myMonth-$myYear, $myHour:$myMinute"
        dateTime="$myDay-$myMonth-$myYear, $myHour:$myMinute"
        binding?.grpAddDevice?.visibility= View.VISIBLE
    }

}
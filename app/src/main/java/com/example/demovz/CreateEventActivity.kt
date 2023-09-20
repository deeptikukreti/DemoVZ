package com.example.demovz

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.demovz.adapter.DevicesListAdapter
import com.example.demovz.databinding.ActivityCreateEventBinding
import java.text.DateFormat
import java.util.Calendar

class CreateEventActivity : AppCompatActivity(),DevicesListAdapter.OnItemClickListener,
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var binding :ActivityCreateEventBinding?=null
    var deviceList =ArrayList<String>()
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
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding=ActivityCreateEventBinding.inflate(layoutInflater)
         setContentView(binding?.root)
         val name = intent.extras?.getString("EVENT_NAME")
         binding?.txtGroupName?.text=name
         viewInitialization()
    }

    private fun viewInitialization() {
        deviceAdapter = DevicesListAdapter(deviceList).apply{ setOnClickListener(this@CreateEventActivity) }
        binding?.rvGrp?.adapter=deviceAdapter
        binding?.apply {
          rgTriggerType.setOnCheckedChangeListener { radioGroup, i ->
              when(i){
                  R.id.rb_time_based->{ grpSelectDateTime.visibility=View.VISIBLE}
                  R.id.rb_event_based->{}
              }
          }

           tvDateTime.setOnClickListener {
               val calendar: Calendar = Calendar.getInstance()
               day = calendar.get(Calendar.DAY_OF_MONTH)
               month = calendar.get(Calendar.MONTH)
               year = calendar.get(Calendar.YEAR)
               val datePickerDialog =
                   DatePickerDialog(this@CreateEventActivity, this@CreateEventActivity, year, month,day)
               datePickerDialog.show()
              // grpAddDevice.visibility=View.VISIBLE
           }

            txtAddDevices.setOnClickListener {
                createAlertDialog()
            }
        }
    }

    fun createAlertDialog(){
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.add_device_layout,null)
        val  device_name = view.findViewById<EditText>(R.id.edt_device)
        val  save = view.findViewById<Button>(R.id.btn_save)
        val  cancel = view.findViewById<Button>(R.id.btn_cancel)
        builder.setView(view)
        save.setOnClickListener {
            deviceList.add(device_name.text.toString())
            deviceAdapter.notifyDataSetChanged()
            if(deviceList.size>0)
                binding?.btnSaveEvent?.visibility=View.VISIBLE
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

    override fun onDeviceRemoved(position: Int) {
        deviceList.removeAt(position)
        deviceAdapter.notifyDataSetChanged()
        if(deviceList.size>0)
            binding?.btnSaveEvent?.visibility=View.VISIBLE
        else
            binding?.btnSaveEvent?.visibility=View.GONE
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this@CreateEventActivity, this@CreateEventActivity, hour, minute,true)
        timePickerDialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        binding?.tvDateTime?.text = "$myDay-$myMonth-$myYear, $myHour:$myMinute"
        binding?.grpAddDevice?.visibility=View.VISIBLE
    }

}
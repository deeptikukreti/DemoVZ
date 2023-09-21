package com.example.demovz

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.demovz.adapter.GroupListAdapter
import com.example.demovz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GroupListAdapter.OnItemClickListener {
    var binding :ActivityMainBinding?=null
    var groupList = ArrayList<String>()
    lateinit var grpAdapter: GroupListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //groupList.addAll(arrayOf("Movie Time","Group 2","Group3"))
        grpAdapter = GroupListAdapter(groupList).apply{ setOnClickListener(this@MainActivity) }
        binding?.rvGrp?.adapter=grpAdapter
        binding?.addBtn?.setOnClickListener {
            createAlertDialog()
        }
    }

    @SuppressLint("MissingInflatedId")
    fun createAlertDialog(){
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.create_event_layout,null)
        val  event_name = view.findViewById<EditText>(R.id.edt_event_name)
        val  next = view.findViewById<Button>(R.id.btn_next)
        val  cancel = view.findViewById<Button>(R.id.btn_cancel)
        builder.setView(view)
        next.setOnClickListener {
            groupList.add(event_name.text.toString())
            grpAdapter.notifyDataSetChanged()
            builder.dismiss()
            startActivity(Intent(this,CreateEventActivity::class.java).putExtra("EVENT_NAME",event_name.text.toString()))
        }
        cancel.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
    override fun onClicked(groupName:String) {
//        startActivity(Intent(this,zGroupDetailActivity::class.java).putExtra("GROUP_NAME",groupName))
    }
}
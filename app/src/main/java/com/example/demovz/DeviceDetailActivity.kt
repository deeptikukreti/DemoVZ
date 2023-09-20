package com.example.demovz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.demovz.adapter.ActionListAdapter
import com.example.demovz.databinding.ActivityDeviceDetailBinding

class DeviceDetailActivity : AppCompatActivity(), ActionListAdapter.OnItemClickListener {
    private var binding: ActivityDeviceDetailBinding? = null
    private var actionList = ArrayList<String>()
    private lateinit var actionAdapter: ActionListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val name = intent.extras?.getString("DEVICE_NAME")
        binding?.txtDeviceName?.text=name
        actionList.addAll(arrayOf("Action 1", "Action 2", "Action 3"))
        actionAdapter =
            ActionListAdapter(actionList).apply { setOnClickListener(this@DeviceDetailActivity) }
        binding?.rvGrp?.adapter = actionAdapter
        binding?.addBtn?.setOnClickListener { createAlertDialog(true,0) }
    }

    fun createAlertDialog(isAdd:Boolean,position: Int) {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.add_action_layout, null)
        val txt_action = view.findViewById<TextView>(R.id.txt_add_edit_action)
        val action_name = view.findViewById<EditText>(R.id.add_edt_action)
        val save = view.findViewById<Button>(R.id.btn_save)
        val cancel = view.findViewById<Button>(R.id.btn_cancel)
        if(!isAdd)
        {
            txt_action.text="Edit Action"
            action_name.setText(actionList.get(position))
        }
        builder.setView(view)
        save.setOnClickListener {
            if(isAdd)
            actionList.add(action_name.text.toString())
            else
            {
                actionList.removeAt(position)
                actionList.add(position,action_name.text.toString())
            }
            actionAdapter.notifyDataSetChanged()
            builder.dismiss()
        }
        cancel.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    override fun onClicked() {

    }

    override fun onActionRemoved(position: Int) {
        actionList.removeAt(position)
        actionAdapter.notifyDataSetChanged()
    }

    override fun onActionEdit(position: Int) {
        createAlertDialog(false,position)
    }
}
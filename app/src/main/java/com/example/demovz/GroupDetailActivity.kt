package com.example.demovz

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.demovz.adapter.DevicesListAdapter
import com.example.demovz.databinding.ActivityGroupDetailBinding
import com.example.demovz.workManager.NotificationWorker

class GroupDetailActivity : AppCompatActivity(), DevicesListAdapter.OnItemClickListener {
    var binding: ActivityGroupDetailBinding? = null
    var deviceList = ArrayList<String>()
    lateinit var deviceAdapter: DevicesListAdapter
    private val workManager by lazy {
        WorkManager.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val name = intent.extras?.getString("GROUP_NAME")
        binding?.txtGroupName?.text = name
        deviceList.addAll(arrayOf("TV", "Led Lights"))
        deviceAdapter =
            DevicesListAdapter(deviceList).apply { setOnClickListener(this@GroupDetailActivity) }
        binding?.rvGrp?.adapter = deviceAdapter

        binding?.addBtn?.setOnClickListener {
            createAlertDialog()
        }


    }

    private fun createOneTimeWorkRequest() {
        // 1
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()
        val imageWorker = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setConstraints(constraints)
            .addTag("noticeWork")
            .build()
        // 2
        workManager.enqueueUniqueWork(
            "oneTimeImageDownload",
            ExistingWorkPolicy.KEEP,
            imageWorker
        )
    }


    @SuppressLint("MissingInflatedId")
    fun createAlertDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.add_device_layout, null)
        val device_name = view.findViewById<EditText>(R.id.edt_device)
        val save = view.findViewById<Button>(R.id.btn_save)
        val cancel = view.findViewById<Button>(R.id.btn_cancel)
        builder.setView(view)
        save.setOnClickListener {
            deviceList.add(device_name.text.toString())
            deviceAdapter.notifyDataSetChanged()
            builder.dismiss()
        }
        cancel.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    override fun onClicked(s: String) {
        startActivity(Intent(this, DeviceDetailActivity::class.java).putExtra("DEVICE_NAME", s))
    }

    override fun onDeviceRemoved(position: Int) {
        deviceList.removeAt(position)
        deviceAdapter.notifyDataSetChanged()
    }
}
package com.example.demovz.ui.device

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demovz.adapter.DevicesListAdapter
import com.example.demovz.databinding.FragmentDeviceBinding
import com.example.demovz.db.Device

class DeviceFragment : Fragment(), DevicesListAdapter.OnItemClickListener {

    private lateinit var viewModel: DeviceViewModel
    private var _binding: FragmentDeviceBinding? = null
    private val binding get() = _binding!!
    private lateinit var deviceAdapter: DevicesListAdapter
    private var deviceList = ArrayList<Device>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[DeviceViewModel::class.java]
        _binding = FragmentDeviceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val device1 = Device("TV", "On")
        val device2 = Device("Led", "On")
        val device3 = Device("Lights", "OFF")
        deviceList.add(device1)
        deviceList.add(device2)
        deviceList.add(device3)

        deviceAdapter =
            DevicesListAdapter(deviceList, false).apply { setOnClickListener(this@DeviceFragment) }
        binding.rvGrp.adapter = deviceAdapter

        return root
    }

    override fun onClicked(s: String) {
        
    }

    override fun onToggleClicked(s: String, action: String, position: Int) {
        deviceList[position].action=action
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDeviceRemoved(position: Int) {
        deviceList.removeAt(position)
        deviceAdapter.notifyDataSetChanged()
    }

}
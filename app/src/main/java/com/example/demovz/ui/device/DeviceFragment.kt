package com.example.demovz.ui.device

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demovz.adapter.addDevice.AreaAdapter
import com.example.demovz.databinding.FragmentDeviceBinding
import com.example.demovz.db.devices.Area
import com.example.demovz.db.devices.AreaWithDeviceData
import com.example.demovz.db.devices.DevicesRoomDb
import com.example.demovz.util.ArrayListConverter

class DeviceFragment : Fragment(), AreaAdapter.OnItemClickListener {

    private lateinit var viewModel: DeviceViewModel
    private var _binding: FragmentDeviceBinding? = null
    private val binding get() = _binding!!

    lateinit var areaAdapter: AreaAdapter
    var areaDeviceList = ArrayList<Area>()
    private var selectedDeviceList = ArrayList<AreaWithDeviceData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[DeviceViewModel::class.java]
        _binding = FragmentDeviceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding?.appBar?.apply {
            ivLogo.visibility=View.VISIBLE
            backIcon.visibility=View.GONE
            txtTitle?.text="Devices"
        }

        activity?.let {
            DevicesRoomDb.getInstance(it!!.applicationContext).areaDeviceDao().getAllAreaWithDevices()
                .also { areaDeviceList.addAll(it) }
        }
        areaDeviceList.forEach { area ->
            val deviceList = ArrayListConverter().toStringArrayList(area.deviceList)
            selectedDeviceList.add(
                AreaWithDeviceData(
                    area.areaId,
                    area.areaName,
                    deviceList,
                    false
                )
            )
        }
        areaAdapter = AreaAdapter(selectedDeviceList,false, requireActivity()).apply { this.setOnClickListener(this@DeviceFragment) }
        binding?.rvGrp?.adapter = areaAdapter
        return root
    }

    override fun onClicked(s: String) {
        TODO("Not yet implemented")
    }

    override fun onToggleClicked(areaPos: Int, s: String, action: Boolean, devicePos: Int) {
        selectedDeviceList[areaPos].deviceList[devicePos].action = action
    }

    override fun onDeviceRemoved(areaPos: Int, position: Int) {
        selectedDeviceList[areaPos].deviceList.removeAt(position)
        areaAdapter.notifyDataSetChanged()
    }
    override fun onExpanded(areaPos: Int, isExpanded: Boolean) {
        selectedDeviceList[areaPos].isExpanded=isExpanded
        areaAdapter.notifyDataSetChanged()
    }
}
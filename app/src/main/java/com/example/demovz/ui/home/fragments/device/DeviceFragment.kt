package com.example.demovz.ui.home.fragments.device

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.demovz.ui.event.adapter.addDevice.AreaAdapter
import com.example.demovz.databinding.FragmentDeviceBinding
import com.example.demovz.db.entity.Area
import com.example.demovz.db.entity.AreaWithDeviceData
import com.example.demovz.ui.home.viewmodel.DeviceViewModel
import com.example.demovz.util.ArrayListConverter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceFragment : Fragment(), AreaAdapter.OnItemClickListener {

    private var _binding: FragmentDeviceBinding? = null
    private val viewModel: DeviceViewModel by  activityViewModels()
    private val binding get() = _binding!!
    private lateinit var areaAdapter: AreaAdapter
    private var selectedDeviceList = ArrayList<AreaWithDeviceData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getDeviceData()

        return root
    }

    private fun getDeviceData() {
        viewModel.getDevices().observe(requireActivity(), Observer<List<Area>>{
           it.forEach {a->
               val deviceList = ArrayListConverter().toStringArrayList(a.deviceList)
               selectedDeviceList.add(
                   AreaWithDeviceData(
                       a.areaId,
                       a.areaName,
                       deviceList,
                       false
                   )
               )
           }
            if(::areaAdapter.isInitialized)
            areaAdapter.notifyDataSetChanged()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.apply {
            ivLogo.visibility=View.VISIBLE
            backIcon.visibility=View.GONE
            txtTitle.text="Devices"
        }
        areaAdapter = AreaAdapter(selectedDeviceList,false, requireActivity(), true).apply { this.setOnClickListener(this@DeviceFragment) }
        binding.rvGrp.adapter = areaAdapter
    }

    override fun onToggleClicked(areaPos: Int, s: String, action: Boolean, devicePos: Int) {
        selectedDeviceList[areaPos].deviceList[devicePos].action = action
    }

    override fun onDeviceRemoved(areaPos: Int, position: Int) {
//        selectedDeviceList[areaPos].deviceList.removeAt(position)
//        areaAdapter.notifyDataSetChanged()
    }
    override fun onExpanded(areaPos: Int, isExpanded: Boolean) {
        selectedDeviceList[areaPos].isExpanded=isExpanded
        areaAdapter.notifyDataSetChanged()
    }
}
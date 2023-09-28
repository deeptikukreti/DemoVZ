package com.example.demovz.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demovz.db.entity.Area
import com.example.demovz.db.entity.Device
import com.example.demovz.repo.DeviceRepository
import com.example.demovz.util.ArrayListConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(private val deviceRepository: DeviceRepository) :
    ViewModel() {

    fun setDeviceData() {
        viewModelScope.launch {
            withContext(Dispatchers.Unconfined) {
                val deviceList1 = ArrayList<Device>()
                deviceList1.add(Device("AC", false))
                deviceList1.add(Device("Ceiling Lights", false))
                deviceList1.add(Device("Room Heater", false))
                deviceList1.add(Device("Cabinet Lights", false))
                deviceList1.add(Device("TV", false))
                val area1 =
                    Area(1, "Living Room", ArrayListConverter().fromStringArrayList(deviceList1))
                deviceRepository.insertDevice(area1)
            }

            withContext(Dispatchers.Unconfined) {
                val deviceList2 = ArrayList<Device>()
                deviceList2.add(Device("AC", false))
                deviceList2.add(Device("Ceiling Lights", false))
                deviceList2.add(Device("Room Heater", false))
                deviceList2.add(Device("Cabinet Lights", false))
                deviceList2.add(Device("TV", false))
                val area2 =
                    Area(2, "Bed Room", ArrayListConverter().fromStringArrayList(deviceList2))
                deviceRepository.insertDevice(area2)
            }

            withContext(Dispatchers.Unconfined) {
                val deviceList3 = ArrayList<Device>()
                deviceList3.add(Device("Led Lights", false))
                deviceList3.add(Device("Chimney", false))
                deviceList3.add(Device("Refrigerator", false))
                deviceList3.add(Device("Smart Coffeemaker", false))
                val area3 =
                    Area(3, "Kitchen", ArrayListConverter().fromStringArrayList(deviceList3))
                deviceRepository.insertDevice(area3)
            }

            withContext(Dispatchers.Unconfined) {
                val deviceList4 = ArrayList<Device>()
                deviceList4.add(Device("Led Lights", false))
                deviceList4.add(Device("Room Heater", false))
                deviceList4.add(Device("AC", false))
                deviceList4.add(Device("Ceiling Lights", false))
                val area4 =
                    Area(4, "Dining Area", ArrayListConverter().fromStringArrayList(deviceList4))
                deviceRepository.insertDevice(area4)
            }

            withContext(Dispatchers.Unconfined) {
                val deviceList5 = ArrayList<Device>()
                deviceList5.add(Device("Ceiling Lights", false))
                deviceList5.add(Device("Corner Walls Staircase Lights", false))
                val area5 =
                    Area(5, "Staircase", ArrayListConverter().fromStringArrayList(deviceList5))
                deviceRepository.insertDevice(area5)
            }

            withContext(Dispatchers.Unconfined) {
                val deviceList6 = ArrayList<Device>()
                deviceList6.add(Device("Led Lights", false))
                deviceList6.add(Device("Geyser", false))
                val area6 =
                    Area(6, "Washroom", ArrayListConverter().fromStringArrayList(deviceList6))
                deviceRepository.insertDevice(area6)
            }
        }
    }

    fun getDevices(): MutableLiveData<List<Area>> {
        val deviceList: MutableLiveData<List<Area>> = MutableLiveData()
        viewModelScope.launch {
            deviceList.postValue(deviceRepository.getAllDevices())
        }
        return deviceList
    }

}

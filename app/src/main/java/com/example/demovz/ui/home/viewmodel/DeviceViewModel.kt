package com.example.demovz.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demovz.db.model.Area
import com.example.demovz.db.model.AreaWithDeviceData
import com.example.demovz.db.model.Device
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

//    @Inject
//    var appPreference: AppPreference? = null

    fun setDeviceData() {
       val job= viewModelScope.launch {
           withContext(Dispatchers.Unconfined) {
               val deviceList1 = ArrayList<Device>()
               deviceList1.add(Device("Living Room AC", false))
               deviceList1.add(Device("Living Room Led Lights", false))
               deviceList1.add(Device("Living Room Tube Light", false))
               deviceList1.add(Device("Living Room Fan", false))
               deviceList1.add(Device("TV", false))
               val area1 =
                   Area(1, "Living Room", ArrayListConverter().fromStringArrayList(deviceList1))
               deviceRepository.insertDevice(area1)
           }

            withContext(Dispatchers.Unconfined) {
                val deviceList2 = ArrayList<Device>()
                deviceList2.add(Device("Bed Room AC", false))
                deviceList2.add(Device("Bed Room Led Lights", false))
                deviceList2.add(Device("Bed Room Tube Light", false))
                deviceList2.add(Device("Bed Room Fan", false))
                val area2 =
                    Area(2, "Bed Room", ArrayListConverter().fromStringArrayList(deviceList2))
                deviceRepository.insertDevice(area2)
            }

            withContext(Dispatchers.Unconfined) {
                val deviceList3 = ArrayList<Device>()
                deviceList3.add(Device("Kitchen Lights", false))
                deviceList3.add(Device("Chimney", false))
                deviceList3.add(Device("Refrigerator", false))
                val area3 =
                    Area(3, "Kitchen", ArrayListConverter().fromStringArrayList(deviceList3))
                deviceRepository.insertDevice(area3)
            }
            withContext(Dispatchers.Unconfined) {
                val deviceList4 = ArrayList<Device>()
                deviceList4.add(Device("Lights", false))
                deviceList4.add(Device("Fan", false))
                val area4 =
                    Area(4, "Dining Area", ArrayListConverter().fromStringArrayList(deviceList4))
                deviceRepository.insertDevice(area4)
            }
            withContext(Dispatchers.Unconfined) {
                val deviceList5 = ArrayList<Device>()
                deviceList5.add(Device("Light1", false))
                deviceList5.add(Device("Light2", false))
                val area5 =
                    Area(5, "Staircase", ArrayListConverter().fromStringArrayList(deviceList5))
                deviceRepository.insertDevice(area5)
            }
            withContext(Dispatchers.Unconfined) {
                val deviceList6 = ArrayList<Device>()
                deviceList6.add(Device("Ceiling Light", false))
                deviceList6.add(Device("Hall Light1", false))
                deviceList6.add(Device("Hall Light2", false))
                deviceList6.add(Device("Hall Light3", false))
                deviceList6.add(Device("Hall Light4", false))
                val area6 =
                    Area(6, "Center Hall", ArrayListConverter().fromStringArrayList(deviceList6))
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

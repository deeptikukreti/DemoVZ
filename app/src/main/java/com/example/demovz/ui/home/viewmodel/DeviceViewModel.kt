package com.example.demovz.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demovz.db.entity.Area
import com.example.demovz.db.entity.Device
import com.example.demovz.db.entity.Feature
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
                deviceList1.add(Device("Shades", false,feature = Feature(1,"Shading","80","%")))
                deviceList1.add(Device("TV", false,feature = Feature(1,"Volume","24","%")))
                deviceList1.add(Device("AC", false, feature = Feature(1,"Climate","72","\u2109")))
                deviceList1.add(Device("Ceiling Lights", false,feature = Feature(1,"Brightness","30","%")))
                deviceList1.add(Device("Cabinet Lights", false,feature = Feature(1,"Brightness","20","%")))
                val area1 =
                    Area(1, "Living Room", ArrayListConverter().fromStringArrayList(deviceList1))
                deviceRepository.insertDevice(area1)
            }

            withContext(Dispatchers.Unconfined) {
                val deviceList2 = ArrayList<Device>()
                deviceList2.add(Device("AC", false, feature = Feature(1,"Cooling","30","&#xb0;F")))
                deviceList2.add(Device("Ceiling Lights", false,feature = Feature(1,"Brightness","50","%")))
                deviceList2.add(Device("Room Heater", false,feature = Feature(1,"Heating","30","%")))
                deviceList2.add(Device("Cabinet Lights", false,feature = Feature(1,"Brightness","80","%")))
                deviceList2.add(Device("TV", false,feature = Feature(1,"Volume","15","%")))
                val area2 =
                    Area(2, "Bed Room", ArrayListConverter().fromStringArrayList(deviceList2))
                deviceRepository.insertDevice(area2)
            }

            withContext(Dispatchers.Unconfined) {
                val deviceList3 = ArrayList<Device>()

                deviceList3.add(Device("Chimney", false,feature = Feature(1,"Mode","70","%")))
                deviceList3.add(Device("Cabinet Lights", false,feature = Feature(1,"Brightness","40","%")))
                deviceList3.add(Device("Ceiling Lights", false,feature = Feature(1,"Brightness","80","%")))
                deviceList3.add(Device("Refrigerator", false,feature = Feature(2,"Mode","Normal Mode","%")))
                val area3 =
                    Area(3, "Kitchen", ArrayListConverter().fromStringArrayList(deviceList3))
                deviceRepository.insertDevice(area3)
            }

            withContext(Dispatchers.Unconfined) {
                val deviceList4 = ArrayList<Device>()
                deviceList4.add(Device("Led Lights", false,feature = Feature(1,"Brightness","38","%")))
                deviceList4.add(Device("Room Heater", false,feature = Feature(1,"Heating","60","%")))
                deviceList4.add(Device("AC", false,feature = Feature(1,"Cooling","38","&#xb0;F")))
                deviceList4.add(Device("Ceiling Lights", false,feature = Feature(1,"Brightness","70","%")))
                val area4 =
                    Area(4, "Dining Area", ArrayListConverter().fromStringArrayList(deviceList4))
                deviceRepository.insertDevice(area4)
            }

            withContext(Dispatchers.Unconfined) {
                val deviceList5 = ArrayList<Device>()
                deviceList5.add(Device("Ceiling Lights", false,feature = Feature(1,"Brightness","50","%")))
                deviceList5.add(Device("Corner Walls Staircase Lights", false,feature = Feature(1,"Brightness","50","%")))
                val area5 =
                    Area(5, "Staircase", ArrayListConverter().fromStringArrayList(deviceList5))
                deviceRepository.insertDevice(area5)
            }

            withContext(Dispatchers.Unconfined) {
                val deviceList6 = ArrayList<Device>()
                deviceList6.add(Device("Led Lights", false,feature = Feature(1,"Brightness","70","%")))
                deviceList6.add(Device("Geyser", false,feature = Feature(1,"Heating","40","%")))
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

    fun getDevicesByAreaId(areaId:Int): MutableLiveData<Area> {
        val deviceList: MutableLiveData<Area> = MutableLiveData()
        viewModelScope.launch {
            deviceList.postValue(deviceRepository.getDevicesByAreaId(areaId))
        }
        return deviceList
    }

}

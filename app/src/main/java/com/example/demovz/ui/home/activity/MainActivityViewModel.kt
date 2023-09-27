package com.example.demovz.ui.home.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demovz.db.devices.Area
import com.example.demovz.repo.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val deviceRepository: DeviceRepository) :ViewModel(){

    fun setDeviceData(list : ArrayList<Area>){
        viewModelScope.launch {
            list.forEach {
                deviceRepository.insertDevice(it)
            }
        }
    }

}

package com.example.demovz.repo

import com.example.demovz.db.devices.Area
import com.example.demovz.db.devices.AreaDeviceDao
import javax.inject.Inject

class DeviceRepository @Inject constructor(private val areaDeviceDao:AreaDeviceDao){

        suspend fun  insertDevice(data: Area) = areaDeviceDao.insert(data)

        suspend fun  getAllDevices() = areaDeviceDao.getAllAreaWithDevices()

}
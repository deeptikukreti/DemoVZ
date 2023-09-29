package com.example.demovz.repo

import com.example.demovz.db.entity.Area
import com.example.demovz.db.dao.AreaDeviceDao
import javax.inject.Inject

class DeviceRepository @Inject constructor(private val areaDeviceDao: AreaDeviceDao){

        suspend fun  insertDevice(data: Area) = areaDeviceDao.insert(data)

        suspend fun  getAllDevices() = areaDeviceDao.getAllAreaWithDevices()
        suspend fun  getDevicesByAreaId(areaId:Int) = areaDeviceDao.getDeviceListByArea(areaId)

}
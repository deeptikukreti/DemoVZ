package com.example.demovz.db.devices

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.demovz.db.events.Device


@Entity(tableName = "area_device_table")
data class Area(
    @PrimaryKey
    val areaId: Int , val areaName :String ,
                @ColumnInfo(name = "device_list")
                 val deviceList: String)

data class AreaWithDeviceData(
    val areaId: Int , val areaName :String ,
    var deviceList: ArrayList<Device>)

data class SelectDeviceData(
    val areaId: Int , val areaName :String ,
    val device: Device)


package com.example.demovz.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "area_device_table")
data class Area(
    @PrimaryKey
    val areaId: Int , val areaName :String ,
                @ColumnInfo(name = "device_list")
                 val deviceList: String)

data class AreaWithDeviceData(
    val areaId: Int, val areaName :String,
    var deviceList: ArrayList<Device>, var isExpanded:Boolean=true)

data class SelectDeviceData(
    val areaId: Int , val areaName :String ,
    val device: Device
)

data class Device(val deviceName: String, var action: Boolean, var isSelected: Boolean = false,val feature:Feature)

/*uiType 1= seekbar
* uiType 2= dropdown*/
data class Feature(val uiType:Int,val featureName : String , val defaultFeatureValue:String, val unit:String)

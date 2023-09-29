package com.example.demovz.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_table")
data class Event(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "event_name")
    val eventName: String,
    @ColumnInfo(name = "trigger_type")
    val triggerType: Int,//1=time base ,2=event based
    @ColumnInfo(name = "date_time")
    val dateTime: String,
    @ColumnInfo(name = "is_recurring")
    val isRecurring: Boolean,
    @ColumnInfo(name = "sensor_device")
    val sensorDevice: String,
    @ColumnInfo(name = "selected_area_id")
    val areaId:Int,
    @ColumnInfo(name = "selected_area_name")
    val areaName: String,
    @ColumnInfo(name = "device_list")
    val deviceList: String,
    @ColumnInfo(name = "select_device_list")
    val selectDeviceList: String

)




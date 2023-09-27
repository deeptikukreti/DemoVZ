package com.example.demovz.db.events

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.demovz.util.ArrayListConverter

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
    @ColumnInfo(name = "device_list")
    val deviceList: String,
    @ColumnInfo(name = "select_device_list")
    val selectDeviceList: String

)

data class Device(val deviceName: String, var action: Boolean, var isSelected: Boolean = false)


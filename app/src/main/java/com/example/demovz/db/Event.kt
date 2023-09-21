package com.example.demovz.db

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
    @ColumnInfo(name = "device_list")
    val deviceList: String

)

data class Device(val deviceName: String, var action: String)
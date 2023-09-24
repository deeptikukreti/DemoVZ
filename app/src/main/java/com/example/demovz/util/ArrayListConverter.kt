package com.example.demovz.util

import androidx.room.TypeConverter
import com.example.demovz.db.devices.AreaWithDeviceData
import com.example.demovz.db.events.Device
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class ArrayListConverter {

    @TypeConverter
    fun fromStringArrayList(value: ArrayList<Device>): String {
        if (value == null) {
            return ""
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Device?>?>() {}.type
        return gson.toJson(value, type)
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromStringArrayListAreaWithDevice(value: ArrayList<AreaWithDeviceData>): String {
        if (value == null) {
            return ""
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<AreaWithDeviceData?>?>() {}.type
        return gson.toJson(value, type)
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringArrayList(value: String): ArrayList<Device> {

        if (value == null) {
            return ArrayList<Device>()
        }
        val gson = Gson()
        val type = object :
            TypeToken<List<Device?>?>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter
    fun toStringArrayListAreaWithDevice(value: String): ArrayList<AreaWithDeviceData> {

        if (value == null) {
            return ArrayList<AreaWithDeviceData>()
        }
        val gson = Gson()
        val type = object :
            TypeToken<List<AreaWithDeviceData?>?>() {}.type
        return gson.fromJson(value, type)
    }
}
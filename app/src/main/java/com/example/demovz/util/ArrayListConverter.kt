package com.example.demovz.util

import androidx.room.TypeConverter
import com.example.demovz.db.Device
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
    fun toStringArrayList(value: String): ArrayList<Device> {

        if (value == null) {
            return ArrayList<Device>()
        }
        val gson = Gson()
        val type = object :
            TypeToken<List<Device?>?>() {}.type
        return gson.fromJson(value, type)
    }
}
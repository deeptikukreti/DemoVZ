package com.example.demovz.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.demovz.db.entity.Area


@Dao
interface AreaDeviceDao  {

    @Insert
    fun insert(area: Area) : Long

    @Update
    fun update(area: Area) : Int

    @Delete
    fun delete(area: Area) : Int

    @Query("delete from area_device_table")
    fun deleteAllAreaWithDeviceList()

    @Query("SELECT * FROM area_device_table")
    fun getAllAreaWithDevices():List<Area>

    @Query("SELECT * FROM area_device_table WHERE areaId LIKE:id")
    fun getAreaData(id:Int): Area
}
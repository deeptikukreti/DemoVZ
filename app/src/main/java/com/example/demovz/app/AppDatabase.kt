package com.example.demovz.app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.demovz.db.model.Area
import com.example.demovz.db.model.Event
import com.example.demovz.db.dao.AreaDeviceDao
import com.example.demovz.db.dao.EventDao

@Database(entities = [Event::class, Area::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun deviceDao(): AreaDeviceDao
}
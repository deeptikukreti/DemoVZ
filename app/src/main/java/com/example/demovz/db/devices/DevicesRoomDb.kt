package com.example.demovz.db.devices

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.demovz.db.model.Area
import com.example.demovz.db.dao.AreaDeviceDao


@Database( exportSchema = false,entities = [Area::class], version = 1)
abstract class DevicesRoomDb : RoomDatabase() {

    abstract fun areaDeviceDao(): AreaDeviceDao

    companion object {
        private var instance: DevicesRoomDb? = null

        @Synchronized
        fun getInstance(ctx: Context): DevicesRoomDb {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, DevicesRoomDb::class.java,"area_device_table")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .allowMainThreadQueries()
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }

//        private fun populateDatabase(db: RoomDb) {
//            val eventDao = db.eventDao()
//            subscribeOnBackground {
//                noteDao.insert(Note("title 1", "desc 1", 1))
//                noteDao.insert(Note("title 2", "desc 2", 2))
//                noteDao.insert(Note("title 3", "desc 3", 3))
//
//            }
//        }
    }

//    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
//       // TODO("Not yet implemented")
//    }
//
//    override fun createInvalidationTracker(): InvalidationTracker {
//        //TODO("Not yet implemented")
//    }
//
//    override fun clearAllTables() {
//        //TODO("Not yet implemented")
//    }


}
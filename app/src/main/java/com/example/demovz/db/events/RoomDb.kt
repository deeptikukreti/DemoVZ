package com.example.demovz.db.events

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Event::class], version = 2)
abstract class RoomDb : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        private var instance: RoomDb? = null

        @Synchronized
        fun getInstance(ctx: Context): RoomDb {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, RoomDb::class.java,"event_table")
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
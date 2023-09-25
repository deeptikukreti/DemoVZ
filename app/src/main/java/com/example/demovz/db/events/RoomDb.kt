package com.example.demovz.db.events

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Event::class], version = 1)
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

    }

}
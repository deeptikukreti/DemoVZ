package com.example.demovz.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface EventDao  {

    @Insert
    fun insert(event: Event) : Long

    @Update
    fun update(event: Event) : Int

    @Delete
    fun delete(event: Event) : Int

    @Query("delete from event_table")
    fun deleteAllEvents()

    @Query("SELECT * FROM event_table")
    fun getAllEvents():List<Event>
}
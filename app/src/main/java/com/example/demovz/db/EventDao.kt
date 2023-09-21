package com.example.demovz.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


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
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM event_table WHERE id LIKE:eventId")
    fun getEvent(eventId:Int):Event
}
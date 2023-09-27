package com.example.demovz.repo

import com.example.demovz.db.events.Event
import com.example.demovz.db.events.EventDao
import javax.inject.Inject

class EventRepository @Inject constructor(private val eventDao: EventDao){

        suspend fun  insertEvent(event: Event) = eventDao.insert(event)

        suspend fun  updateEvent(event: Event) = eventDao.update(event)

        suspend fun  deleteEvent(event: Event) = eventDao.delete(event)

        suspend fun  getAllEvents() = eventDao.getAllEvents()

        suspend fun  getEventById(id:Int) = eventDao.getEvent(id)

}
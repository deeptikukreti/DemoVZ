package com.example.demovz.ui.event.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demovz.db.entity.Event
import com.example.demovz.repo.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val eventRepository: EventRepository) :
    ViewModel() {

    fun createEvent(event: Event) {
       viewModelScope.launch {
         eventRepository.insertEvent(event)
        }

    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.updateEvent(event)
        }
    }

    fun getEventById(eventId:Int) :MutableLiveData<Event>{
        val eventData: MutableLiveData<Event> = MutableLiveData()
        viewModelScope.launch {
            eventData.postValue( eventRepository.getEventById(eventId))
        }
        return eventData
    }

    fun deleteEvent(event: Event){
        viewModelScope.launch {
            eventRepository.deleteEvent(event)
        }
    }


    fun getAllEvents(): MutableLiveData<List<Event>> {
        val eventList: MutableLiveData<List<Event>> = MutableLiveData()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                eventList.postValue(eventRepository.getAllEvents())
            }
        }
        return eventList
    }

    fun getEventsByTriggerType(id:Int): MutableLiveData<List<Event>> {
        val eventList: MutableLiveData<List<Event>> = MutableLiveData()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                eventList.postValue(eventRepository.getAllEventsByTriggerId(id))
            }
        }
        return eventList
    }

}

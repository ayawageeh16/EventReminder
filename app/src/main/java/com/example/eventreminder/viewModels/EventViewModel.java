package com.example.eventreminder.viewModels;

import android.app.Application;

import com.example.eventreminder.models.event.EventsModelDTO;
import com.example.eventreminder.repositories.EventRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class EventViewModel extends AndroidViewModel {

    private EventRepository eventRepository;
    private MutableLiveData<EventsModelDTO> events;

    public EventViewModel(@NonNull Application application) {
        super(application);
        eventRepository = new EventRepository(application);
        events = new MutableLiveData<>();
    }

    public MutableLiveData<EventsModelDTO> getEvents() {
        events.setValue(eventRepository.getAllEvents());
        return events;
    }

}

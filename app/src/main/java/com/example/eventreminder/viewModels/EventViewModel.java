package com.example.eventreminder.viewModels;

import android.app.Application;

import com.example.eventreminder.models.event.EventList;
import com.example.eventreminder.repositories.EventRepository;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class EventViewModel extends AndroidViewModel{

    private EventRepository eventRepository;
    private MutableLiveData<List<EventList>> events;

    public EventViewModel(@NonNull Application application) {
          super(application);
          eventRepository = new EventRepository(application);
          events = new MutableLiveData<>();
    }

    public MutableLiveData<List<EventList>> getEvents (){
        events.setValue(eventRepository.getAllEvents());
        return events;
    }

}

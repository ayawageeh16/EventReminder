package com.example.eventreminder.daos;

import com.example.eventreminder.models.event.EventList;

import java.util.List;

public interface EventsDao {
    List<EventList> getAllEvents();
}

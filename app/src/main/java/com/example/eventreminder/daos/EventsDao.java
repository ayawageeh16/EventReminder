package com.example.eventreminder.daos;

import com.example.eventreminder.models.event.EventsModelDTO;

public interface EventsDao {
    EventsModelDTO getAllEvents();
}

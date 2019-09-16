package com.example.eventreminder.daos;

import com.example.eventreminder.models.event.EventsModelDTO;

public interface UpdateDao {

    void sendNewData(EventsModelDTO eventsModelDTO);
}

package com.example.eventreminder.models.event;

import java.io.Serializable;

public class EventAttendessDTO implements Serializable {

    private Item event;
    private String userEmail;

    public EventAttendessDTO() {
    }

    public EventAttendessDTO(Item event, String userEmail) {
        this.event = event;
        this.userEmail = userEmail;
    }

    public Item getEvent() {
        return event;
    }

    public void setEvent(Item event) {
        this.event = event;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

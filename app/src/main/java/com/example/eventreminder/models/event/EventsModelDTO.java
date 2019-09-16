package com.example.eventreminder.models.event;

import java.io.Serializable;
import java.util.List;

public class EventsModelDTO implements Serializable {

    private List<Item> items;
    private String userEmail;

    public EventsModelDTO() {
    }

    public EventsModelDTO(List<Item> items, String userEmail) {
        this.items = items;
        this.userEmail = userEmail;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}

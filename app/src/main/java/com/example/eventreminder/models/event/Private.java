
package com.example.eventreminder.models.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Private implements Serializable {

    @SerializedName("eventAttendeeList")
    @Expose
    private String eventAttendeeList;

    public String getEventAttendeeList() {
        return eventAttendeeList;
    }

    public void setEventAttendeeList(String eventAttendeeList) {
        this.eventAttendeeList = eventAttendeeList;
    }

}

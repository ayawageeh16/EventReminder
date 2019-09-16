
package com.example.eventreminder.models.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateRequest implements Serializable {

    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("conferenceSolutionKey")
    @Expose
    private ConferenceSolutionKey conferenceSolutionKey;
    @SerializedName("status")
    @Expose
    private Status status;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ConferenceSolutionKey getConferenceSolutionKey() {
        return conferenceSolutionKey;
    }

    public void setConferenceSolutionKey(ConferenceSolutionKey conferenceSolutionKey) {
        this.conferenceSolutionKey = conferenceSolutionKey;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}

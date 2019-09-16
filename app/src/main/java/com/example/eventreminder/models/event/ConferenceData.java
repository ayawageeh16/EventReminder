
package com.example.eventreminder.models.event;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConferenceData implements Serializable{

    @SerializedName("createRequest")
    @Expose
    private CreateRequest createRequest;
    @SerializedName("entryPoints")
    @Expose
    private List<EntryPoint> entryPoints = null;
    @SerializedName("conferenceSolution")
    @Expose
    private ConferenceSolution conferenceSolution;
    @SerializedName("signature")
    @Expose
    private String signature;

    public CreateRequest getCreateRequest() {
        return createRequest;
    }

    public void setCreateRequest(CreateRequest createRequest) {
        this.createRequest = createRequest;
    }

    public List<EntryPoint> getEntryPoints() {
        return entryPoints;
    }

    public void setEntryPoints(List<EntryPoint> entryPoints) {
        this.entryPoints = entryPoints;
    }

    public ConferenceSolution getConferenceSolution() {
        return conferenceSolution;
    }

    public void setConferenceSolution(ConferenceSolution conferenceSolution) {
        this.conferenceSolution = conferenceSolution;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}

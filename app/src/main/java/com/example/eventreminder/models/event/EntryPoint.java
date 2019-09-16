
package com.example.eventreminder.models.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EntryPoint implements Serializable {

    @SerializedName("entryPointType")
    @Expose
    private String entryPointType;
    @SerializedName("uri")
    @Expose
    private String uri;

    public String getEntryPointType() {
        return entryPointType;
    }

    public void setEntryPointType(String entryPointType) {
        this.entryPointType = entryPointType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}

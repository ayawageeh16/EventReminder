
package com.example.eventreminder.models.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExtendedProperties implements Serializable {

    @SerializedName("shared")
    @Expose
    private Shared shared;
    @SerializedName("private")
    @Expose
    private Private _private;

    public Shared getShared() {
        return shared;
    }

    public void setShared(Shared shared) {
        this.shared = shared;
    }

    public Private getPrivate() {
        return _private;
    }

    public void setPrivate(Private _private) {
        this._private = _private;
    }

}

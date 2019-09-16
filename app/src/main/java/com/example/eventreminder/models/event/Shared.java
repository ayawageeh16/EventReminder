
package com.example.eventreminder.models.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Shared  implements Serializable {

    @SerializedName("CalendarSyncAdapter#originalTimezone")
    @Expose
    private String calendarSyncAdapterOriginalTimezone;

    public String getCalendarSyncAdapterOriginalTimezone() {
        return calendarSyncAdapterOriginalTimezone;
    }

    public void setCalendarSyncAdapterOriginalTimezone(String calendarSyncAdapterOriginalTimezone) {
        this.calendarSyncAdapterOriginalTimezone = calendarSyncAdapterOriginalTimezone;
    }

}

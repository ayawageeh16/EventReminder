package com.example.eventreminder.repositories;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eventreminder.constants.EventRequestConstants;
import com.example.eventreminder.constants.SharedPreferencesConstant;
import com.example.eventreminder.daos.EventsDao;
import com.example.eventreminder.models.event.EventList;
import com.example.eventreminder.models.event.EventsModelDTO;
import com.example.eventreminder.models.event.Item;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRepository implements EventsDao {


    private final Application application;
    EventsModelDTO eventsModelDTO;
    private Gson gson;
    private String code;

    public EventRepository(Application application) {
        this.application = application;
        gson = new GsonBuilder().serializeNulls().create();

        // Get Access Token From SharedPreferences And Request Events List
        SharedPreferences preferences = application.getSharedPreferences(
                SharedPreferencesConstant.EVENTREMINDER_SHARED_PREFERENCES, application.MODE_PRIVATE);
        code = preferences.getString(SharedPreferencesConstant.ACCESS_TOKEN_SHARED_PREFERENCES, "");
    }

    private void getEventsVolleyRequest() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                EventRequestConstants.EVNET_REQUEST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("eventsRequestresponse", response.toString());

                        // Parse Json To EventList
                        EventList eventsItems = gson.fromJson(response.toString(), EventList.class);

                        // Sort Events By Created Date Descending
                        List<Item> events = eventsItems.getItems();
                        sortEventsDescending(events);

                        // Set RecyclerView Adapter Data
                        eventsModelDTO = new EventsModelDTO(events, eventsItems.getSummary());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("eventsRequestError", "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", "Bearer " + code);
                return headers;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, EventRequestConstants.TAG_EVENTS_REQUEST);
    }

    private List<Item> sortEventsDescending(List<Item> events) {
        Collections.sort(events, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return item2.getCreated().compareTo(item1.getCreated());
            }
        });
        return events;
    }

    @Override
    public EventsModelDTO getAllEvents() {
        if (!code.equalsIgnoreCase("")) {
            getEventsVolleyRequest();
        }
        return eventsModelDTO;
    }


    // TODO Find Overlapped Events
    private boolean chechForOverlapping(String start1, String end1, String start2, String end2) throws ParseException {

        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(start1);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(start1);
        Date date3 = new SimpleDateFormat("dd/MM/yyyy").parse(start1);
        Date date4 = new SimpleDateFormat("dd/MM/yyyy").parse(start1);

        return date1.getTime() <= date2.getTime() && date3.getTime() <= date4.getTime();
    }
}

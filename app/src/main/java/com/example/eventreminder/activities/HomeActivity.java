package com.example.eventreminder.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eventreminder.R;
import com.example.eventreminder.adapters.EventsRecyclerViewAdapter;
import com.example.eventreminder.constants.EventRequestConstants;
import com.example.eventreminder.constants.SharedPreferencesConstant;
import com.example.eventreminder.models.event.EventAttendessDTO;
import com.example.eventreminder.models.event.EventList;
import com.example.eventreminder.models.event.EventsModelDTO;
import com.example.eventreminder.models.event.Item;
import com.example.eventreminder.repositories.AppController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity {

    // UI Components
    private RecyclerView eventsRecyclerView;

    private EventsRecyclerViewAdapter eventsRecyclerViewAdapter;
    private Gson gson;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Custom ActionBar Color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#e74c3c")));

        // RecyclerView Setup
        eventsRecyclerView = findViewById(R.id.events_recyclerView);
        setRecyclerView();

        gson = new GsonBuilder().serializeNulls().create();

        // Get Access Token From SharedPreferences And Request Events List
        SharedPreferences preferences = this.getSharedPreferences(
                SharedPreferencesConstant.EVENTREMINDER_SHARED_PREFERENCES, this.MODE_PRIVATE);
        code =  preferences.getString(SharedPreferencesConstant.ACCESS_TOKEN_SHARED_PREFERENCES, "");
        if(!code.equalsIgnoreCase("")) {
            getEventsVolleyRequest();
        }

        // Pass Current Event Object tO Details Activity
        eventsRecyclerViewAdapter.setListener(new EventsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EventAttendessDTO event) {
                Intent intent = new Intent(HomeActivity.this, EventDetails.class);
                intent.putExtra("event",event);
                startActivity(intent);
            }
        });
    }

    /**
     * This Method Setup Events RecyclerView
     */
    private void setRecyclerView(){
        eventsRecyclerViewAdapter = new EventsRecyclerViewAdapter(this);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecyclerView.setHasFixedSize(true);
    }

    /**
     * This Method Uses Volley JsonObject Request to Retrieve All User's Events
     * Using Google Calender API
     */
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
                        EventsModelDTO eventsModelDTO = new EventsModelDTO(events,eventsItems.getSummary());
                        eventsRecyclerViewAdapter.setEvents(eventsModelDTO);
                        eventsRecyclerView.setAdapter(eventsRecyclerViewAdapter);
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

    /**
     * This Method Sorts The List Of Events According To Its Created Date Attribute Descending
     * @param  events List<Item>
     * @return Sorted List<Item>
     */
    private List<Item> sortEventsDescending (List<Item> events){
        Collections.sort(events, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return item2.getCreated().compareTo(item1.getCreated());
            }
        });
        return events;
    }

}

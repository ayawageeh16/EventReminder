package com.example.eventreminder.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.eventreminder.R;
import com.example.eventreminder.constants.SharedPreferencesConstant;
import com.example.eventreminder.constants.UpdateEventStatusRequestConstants;
import com.example.eventreminder.constants.WeatherRequestConstants;
import com.example.eventreminder.models.event.Attendee;
import com.example.eventreminder.models.event.EventAttendessDTO;
import com.example.eventreminder.models.weather.WeatherModel;
import com.example.eventreminder.repositories.AppController;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class EventDetails extends AppCompatActivity {

    private static final String WEATHER_KEY = "&appid=f9678b9928fef1dabb89c00e3fa25d92";

    // UI Components
    private TextView eventTitleTv;
    private TextView eventStartTimeTv;
    private TextView eventEndTimeTv;
    private TextView eventDateTv;
    private TextView eventWeatherTv;
    private ImageView weatherIconImageView;
    private ImageView eventHeaderImageView;
    private TextView eventTempMaxTextView;
    private TextView eventTempMinTextView;
    private LinearLayout weatherLayout;
    private LinearLayout weatherIconLayout;
    private LinearLayout acceptEvent;
    private LinearLayout declineEvent;
    private LinearLayout acceptDeclineLayout;
    private CoordinatorLayout detailsLayout;
    private Snackbar snackbar;

    private Gson gson;
    private String city;
    private String userEmail;
    private String creatorEmail;
    private EventAttendessDTO event;
    private Attendee userAttendee;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Custom ActionBar Color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#e74c3c")));

        eventTitleTv = findViewById(R.id.event_title_details_tv);
        eventStartTimeTv = findViewById(R.id.event_start_details_tv);
        eventEndTimeTv = findViewById(R.id.event_end_details_tv);
        eventDateTv = findViewById(R.id.event_date_details_tv);
        eventWeatherTv = findViewById(R.id.event_weather_details_tv);
        weatherIconImageView = findViewById(R.id.weather_icon_img);
        eventTempMaxTextView = findViewById(R.id.event_tempMax_tv);
        eventTempMinTextView = findViewById(R.id.event_temprMin_tv);
        eventHeaderImageView = findViewById(R.id.header_img);
        weatherLayout = findViewById(R.id.weather_data_lineatlayout);
        weatherIconLayout = findViewById(R.id.weather_icon_linearLayout);
        acceptEvent = findViewById(R.id.accept_event);
        declineEvent = findViewById(R.id.decline_event);
        detailsLayout = findViewById(R.id.details_layout);
        acceptDeclineLayout = findViewById(R.id.accept_decline_linearLayout);

        gson = new GsonBuilder().serializeNulls().create();

        // Get Passed Event Object And Display Data
        Bundle bundle = getIntent().getExtras();
        event = (EventAttendessDTO) bundle.getSerializable("event");
        setView();

        // Get Access Token From SharedPreferences
        SharedPreferences preferences = this.getSharedPreferences(
                SharedPreferencesConstant.EVENTREMINDER_SHARED_PREFERENCES, this.MODE_PRIVATE);
        code = preferences.getString(SharedPreferencesConstant.ACCESS_TOKEN_SHARED_PREFERENCES, "");

        acceptEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!code.equalsIgnoreCase("")) {
                    AcceptOrDeclineEventVolleyRequest("accepted");
                }
                dismissSnackBar();
            }
        });

        declineEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!code.equalsIgnoreCase("")) {
                    AcceptOrDeclineEventVolleyRequest("declined");
                }
                dismissSnackBar();
            }
        });

    }

    /**
     * This Method Displays Event Details
     */
    private void setView() {

        Glide.with(EventDetails.this)
                .load(R.drawable.bkg_10_october)
                .into(eventHeaderImageView);

        eventTitleTv.setText(event.getEvent().getSummary());
        eventStartTimeTv.setText("Starts:" + getTime(event.getEvent().getStart().getDateTime()));
        eventEndTimeTv.setText("Ends:" + getTime(event.getEvent().getEnd().getDateTime()));

        // Event Date Setup
        String date = event.getEvent().getStart().getDate();
        if (date != null && !date.isEmpty()) {
            eventDateTv.setText(date);
        } else {
            eventDateTv.setText(getDate(getDate(event.getEvent().getStart().getDateTime())));
        }

        // Get User's Location if Found or Get Location From Time Zone
        if (event.getEvent().getLocation() != null && !event.getEvent().getLocation().isEmpty()) {
            String[] location = event.getEvent().getLocation().split(",");
            city = location[0];

        } else if (event.getEvent().getStart().getTimeZone() != null && !event.getEvent().getStart().getTimeZone().isEmpty()) {
            String[] location = event.getEvent().getStart().getTimeZone().split("\\/");
            city = location[1];
        }

        // Get Event's Weather
        if (city != null && !city.isEmpty()) {
            weatherLayout.setVisibility(View.VISIBLE);
            weatherIconLayout.setVisibility(View.VISIBLE);
            getWeatherVolleyRequest(city);
        }

        // Ask User to Confirm The Event If Needed
        if (!event.getUserEmail().equals(event.getEvent().getCreator().getEmail())) {
            for (Attendee attendee : event.getEvent().getAttendees()) {
                String userEmail = attendee.getEmail();
                if (event.getUserEmail().equals(userEmail)) {
                    if (attendee.getResponseStatus().equalsIgnoreCase("needsAction")) {
                        userAttendee = attendee;
                        acceptDeclineLayout.setVisibility(View.VISIBLE);
                        showSnakeBar();
                        break;
                    }
                }
            }
        }

    }

    /**
     * This Method Show SnakeBar
     */
    private void showSnakeBar() {
        snackbar = Snackbar.make(detailsLayout, R.string.item_removed_message, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    /**
     * This Method Dismiss SnackBar
     */
    private void dismissSnackBar() {
        snackbar.dismiss();
    }

    /**
     * This Method Extract Date And Return Right Date Format
     * @param originalString
     * @return String Date
     */
    private String getDate(String originalString) {
        if (originalString != null) {
            return originalString.split("T")[0];
        }
        return "Not specified";
    }

    /**
     * This Method Extract Time And Return Time Date Format
     * @param originalString
     * @return String Time
     */
    private String getTime(String originalString) {
        if (originalString != null) {
            String[] dateAndTimeArray = originalString.split("T");
            String[] timeArray = dateAndTimeArray[1].split(":");
            return timeArray[0] + ":" + timeArray[1];
        }
        return "Not specified";
    }

    /**
     * This Method Implements Weather Request
     * @param city String location if found or Event Time Zone City
     */
    private void getWeatherVolleyRequest(final String city) {

        StringBuilder URL = new StringBuilder(WeatherRequestConstants.WEATHER_MAIN_API_URL);
        URL.append("q=").append(city).append(WeatherRequestConstants.WEATHER_KEY);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("weatherVolleyresponse", response.toString());
                        // Parse Json Object To WeatherModel And Display Data
                        WeatherModel weatherModel = gson.fromJson(response.toString(), WeatherModel.class);
                        setWeatherViews(weatherModel);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("weathervolleyError", "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    /**
     * This Method Displays Weather Data
     * @param weather
     */
    private void setWeatherViews(WeatherModel weather) {
        eventWeatherTv.setText(weather.getWeather().get(0).getDescription());
        eventTempMaxTextView.setText(new DecimalFormat("##.##").format((weather.getMain().getTempMax() - 273.15)) + "°");
        eventTempMinTextView.setText(new DecimalFormat("##.##").format((weather.getMain().getTempMin() - 273.15)) + "°");
        String iconCode = weather.getWeather().get(0).getIcon();
        String iconUri = "http://openweathermap.org/img/w/" + iconCode +
                ".png";
        Glide.with(EventDetails.this)
                .load(iconUri)
                .into(weatherIconImageView);
    }

    /**
     * This Method Implements Accept Or Decline Event Logic
     * @param updatedStatus
     */
    private void AcceptOrDeclineEventVolleyRequest(final String updatedStatus) {

        StringBuilder URL = new StringBuilder(UpdateEventStatusRequestConstants.UPDATE_EVENT_STATUS_MAIN_URI);
        URL.append(event.getEvent().getId()).append(UpdateEventStatusRequestConstants.API_KEY);

        // Create Request Body
        JSONObject jsonBodyObj = new JSONObject();
        try {
            for (Attendee attendee : event.getEvent().getAttendees()) {
                String userEmail = attendee.getEmail();
                if (event.getUserEmail().equals(userEmail)) {
                    attendee.setResponseStatus(updatedStatus);
                    break;
                }
            }
            String start = gson.toJson(event.getEvent().getStart());
            String end = gson.toJson(event.getEvent().getEnd());
            String attendees = gson.toJson(event.getEvent().getAttendees());
            jsonBodyObj.put("end", end);
            jsonBodyObj.put("start", start);
            jsonBodyObj.put("attendees", attendees);

            Log.i("updareurl", URL.toString() + "  " + "start :" + start + "end: " + end + "attendees[]: " + attendees);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBodyObj.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                URL.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("JSONPost", response.toString());
                acceptDeclineLayout.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", "Bearer " + code);
                return headers;
            }

            // this is the relevant method
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
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


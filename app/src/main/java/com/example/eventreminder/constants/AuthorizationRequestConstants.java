package com.example.eventreminder.constants;

public class AuthorizationRequestConstants {
    public static final String CLIENT_ID = "969174132651-mm8a60f8jm1i8bj24c8mov3c1rj3qs01.apps.googleusercontent.com";
    public static final String REDIRECT_URI = "http://localhost";
    public static final String SCOPE = "https://www.googleapis.com/auth/calendar.events";
    public static final String AUTH_URI = "https://accounts.google.com/o/oauth2/auth";
    public static final String TOKEN_END_POINT = "https://www.googleapis.com/oauth2/v4/token";
    public static final String TOKEN_REQUEST_URI = AUTH_URI + "?client_id=" +CLIENT_ID + "&response_type=code&scope=" +SCOPE + "&redirect_uri=" +REDIRECT_URI;

}

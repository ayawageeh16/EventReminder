package com.example.eventreminder.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eventreminder.R;
import com.example.eventreminder.constants.AuthorizationRequestConstants;
import com.example.eventreminder.constants.SharedPreferencesConstant;
import com.example.eventreminder.repositories.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    private String code;

    // UI Components
    private Button getToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBar();
        setContentView(R.layout.activity_main);
        getToken = findViewById(R.id.loginBtn);

        getToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AuthorizationRequestConstants.TOKEN_REQUEST_URI));
                startActivity(intent);
            }
        });

    }

    private void setActionBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(AuthorizationRequestConstants.REDIRECT_URI)) {
            code = uri.getQueryParameter("code");
            String error = uri.getQueryParameter("error");
            if (!TextUtils.isEmpty(code)) {
                String tokenUrl = AuthorizationRequestConstants.TOKEN_END_POINT +
                        "?code=" + code +
                        "&client_id=" + AuthorizationRequestConstants.CLIENT_ID +
                        "&redirect_uri=" + AuthorizationRequestConstants.REDIRECT_URI + "&grant_type=authorization_code";
                requestToken(tokenUrl);
            } else if (!TextUtils.isEmpty(error)) {
                // user rejected
                Toast.makeText(this, " Please allow the app to access your calender to get Started!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestToken(String  tokenUrl) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                tokenUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("volleyresponse", response.toString());

                        try {
                            String accessToken = response.getString("access_token");
                            saveTokenInSharedPreferences(accessToken);

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra("accessToken", accessToken);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volleyError", "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void saveTokenInSharedPreferences(String accessToken){
        SharedPreferences.Editor editor = getSharedPreferences(SharedPreferencesConstant.EVENTREMINDER_SHARED_PREFERENCES
                , MODE_PRIVATE).edit();
        editor.putString(SharedPreferencesConstant.ACCESS_TOKEN_SHARED_PREFERENCES, accessToken);
        editor.apply();
    }
}

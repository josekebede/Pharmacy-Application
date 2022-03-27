package com.example.pharmacyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

public class LoginActivity extends AppCompatActivity {

    EditText username, password, txtIp;
    Button login;
    DatabaseHelper db;

    private void authenticateUser() {
        db.setIP(txtIp.getText().toString());
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = MessageFormat.format("http://{0}:8000/auth", db.getIP());

            JSONObject requestBodyJson = new JSONObject();
            String requestBody;

            requestBodyJson.put("username", username.getText().toString());
            requestBodyJson.put("password", password.getText().toString());
            requestBody = requestBodyJson.toString();


// Request a string response from the provided URL.
            Request stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            login.setEnabled(true);
                            username.setEnabled(true);
                            password.setEnabled(true);
                            txtIp.setEnabled(true);
                            if (response.equals("Found")) {
                                Toasting("Login Correct");
                                goToActivity();
                            } else {
                                Toasting("Incorrect Login Information");
                            }



                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toasting(error.toString());
                    login.setEnabled(true);
                    username.setEnabled(true);
                    password.setEnabled(true);
                    txtIp.setEnabled(true);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkIP() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = MessageFormat.format("http://{0}:8000/get", txtIp.getText());
        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        login.setEnabled(true);
                        username.setEnabled(true);
                        password.setEnabled(true);
                        txtIp.setEnabled(true);

                        if(response.equals("works"))
                            authenticateUser();
                        else
                            Toasting(response);
//                            Toasting("Incorrect IP in Response " + url);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toasting("Incorrect IP");
                        login.setEnabled(true);
                        username.setEnabled(true);
                        password.setEnabled(true);
                        txtIp.setEnabled(true);

                    }
                });
        queue.add(stringRequest);
    }
    public void Toasting(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void goToActivity() {
        username.setText("");
        password.setText("");
        Intent intent = new Intent(this, MainActivity.class);
        // TODO - Add information to intent like name
        startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(getApplicationContext());
        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        login = (Button) findViewById(R.id.btnLogin);
        txtIp = (EditText) findViewById(R.id.txtIp);

        txtIp.setText(db.getIP());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setEnabled(false);
                username.setEnabled(false);
                password.setEnabled(false);
                txtIp.setEnabled(false);
                checkIP();
            }
        });

    }
}
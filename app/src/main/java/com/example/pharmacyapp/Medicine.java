package com.example.pharmacyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

public class Medicine extends AppCompatActivity {

    EditText txtName;
    EditText txtDescription;
    EditText txtStock;
    Button saveButton;
    Button deleteButton;
    Button discardButton;
    Bundle extras;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        extras = getIntent().getExtras();
        txtName = (EditText) findViewById(R.id.txtMedName);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtStock = (EditText) findViewById(R.id.txtStock);
        saveButton = (Button) findViewById(R.id.saveButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        discardButton = (Button) findViewById(R.id.discardButton);
        db = new DatabaseHelper(this);

        if (extras != null) {
            saveButton.setEnabled(false);
            txtName.setEnabled(false);
            txtName.setText(extras.getString("name"));
            txtDescription.setText(extras.getString("description"));
            txtStock.setText(extras.getString("stock"));
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Medicine.super.finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtName.getText().toString().trim().isEmpty() ||
                        txtDescription.getText().toString().trim().isEmpty() ||
                        txtStock.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Empty fields are not allowed", Toast.LENGTH_LONG).show();
                } else {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = MessageFormat.format("http://{0}:8000/addMedicine", db.getIP());
                    JSONObject requestBodyJson = new JSONObject();
                    try {
                        requestBodyJson.put("name", txtName.getText().toString().trim());
                        requestBodyJson.put("description", txtDescription.getText().toString().trim());
                        requestBodyJson.put("stock", txtStock.getText().toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String requestBody = requestBodyJson.toString();

                    StringRequest stringRequest = new StringRequest
                            (Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                    Medicine.super.finish();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Error Saving Medicine", Toast.LENGTH_LONG).show();
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
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = MessageFormat.format("http://{0}:8000/deleteMedicine", db.getIP());
                JSONObject requestBodyJson = new JSONObject();
                try {
                    requestBodyJson.put("name", txtName.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String requestBody = requestBodyJson.toString();

                StringRequest stringRequest = new StringRequest
                        (Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                Medicine.super.finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Error Deleting Medicine", Toast.LENGTH_LONG).show();
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
            }
        });
    }
}
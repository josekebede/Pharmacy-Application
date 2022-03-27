package com.example.pharmacyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList;
    JSONArray resArray;
    DatabaseHelper db;
    JSONArray medicineInformation;
    EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        medicineInformation = new JSONArray();
        txtSearch = (EditText)findViewById(R.id.txtSearch);

        db = new DatabaseHelper(getApplicationContext());
        getMedicineList();

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().isEmpty()){
                    getMedicineList();
                } else {
                    ArrayList filteredMedicine = filterMedicine(charSequence.toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, filteredMedicine);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        getMedicineList();

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_button:
                Intent intent = new Intent(getApplicationContext(), Medicine.class);
                startActivity(intent);
                return true;
            case R.id.logout_button:
                MainActivity.super.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Toasting(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void getMedicineList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = MessageFormat.format("http://{0}:8000/medicine", db.getIP());
        resArray = new JSONArray();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        medicineInformation = response;
                        arrayList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                arrayList.add(medicineInformation.getJSONObject(i).getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    goToDescriptionDetails(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toasting("Error in medicine list. Contact your administrator.");
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void goToDescriptionDetails(int medicineIndex) throws JSONException {
        Intent intent = new Intent(getApplicationContext(), Medicine.class);
        intent.putExtra("name", medicineInformation.getJSONObject(medicineIndex).getString("name"));
        intent.putExtra("description", medicineInformation.getJSONObject(medicineIndex).getString("Description"));
        intent.putExtra("stock", medicineInformation.getJSONObject(medicineIndex).getString("stock"));
        startActivity(intent);
    }

    private ArrayList filterMedicine(String keyword) {
        ArrayList filtered = new ArrayList();
        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).toString().toLowerCase().contains(keyword.toLowerCase())){
                filtered.add(arrayList.get(i));
            }
        }
        return filtered;
    }


}
package com.example.hospitalappointmentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hospitalappointmentapp.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    
    // Node.js API endpoint (Works via 'adb reverse tcp:3000 tcp:3000')
    private static final String LOGIN_URL = "http://localhost:3000/api/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Click event for Login button
        binding.btnLogin.setOnClickListener(v -> performLogin());

        // Click event to navigate to Register Activity
        binding.tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin() {
        String email = binding.etLoginEmail.getText().toString().trim();
        String password = binding.etLoginPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build JSON body for request
        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Send HTTP Request to Node.js Backend using Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                LOGIN_URL,
                postData,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONObject user = response.getJSONObject("user");
                            String role = user.getString("role"); // Patient, Nurse, or Doctor

                            Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                            // Redirect user based on their role from the database
                            Intent intent;
                            if (role.equalsIgnoreCase("Doctor")) {
                                intent = new Intent(MainActivity.this, DoctorDashboardActivity.class);
                            } else if (role.equalsIgnoreCase("Nurse")) {
                                intent = new Intent(MainActivity.this, NurseDashboardActivity.class);
                            } else {
                                intent = new Intent(MainActivity.this, PatientDashboardActivity.class);
                            }
                            startActivity(intent);
                            finish(); // Prevents user from going back to login screen on back press
                        } else {
                             Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String message = "Server Error. Check ADB reverse or Node server.";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        message = new String(error.networkResponse.data);
                    }
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
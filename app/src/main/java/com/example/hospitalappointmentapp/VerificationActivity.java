package com.example.hospitalappointmentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hospitalappointmentapp.databinding.ActivityVerificationBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class VerificationActivity extends AppCompatActivity {

    private ActivityVerificationBinding binding;
    private String userEmail;

    private static final String VERIFY_URL = "http://localhost:3000/api/verify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userEmail = getIntent().getStringExtra("USER_EMAIL");

        binding.btnVerify.setOnClickListener(v -> performVerification());
    }

    private void performVerification() {
        String code = binding.etVerificationCode.getText().toString().trim();

        if (code.isEmpty() || code.length() < 6) {
            Toast.makeText(this, R.string.enter_6_digit_code, Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", userEmail);
            postData.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                VERIFY_URL,
                postData,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(this, R.string.verification_successful, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, R.string.error_invalid_code, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, R.string.server_error, Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);
    }
}
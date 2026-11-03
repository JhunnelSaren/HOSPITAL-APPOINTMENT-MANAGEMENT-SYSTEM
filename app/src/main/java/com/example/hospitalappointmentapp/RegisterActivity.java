package com.example.hospitalappointmentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hospitalappointmentapp.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private static final String REGISTER_URL = "http://localhost:3000/api/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(v -> performRegistration());

        binding.tvGoToLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private void performRegistration() {
        String name = binding.etRegName.getText().toString().trim();
        String email = binding.etRegEmail.getText().toString().trim();
        String password = binding.etRegPassword.getText().toString().trim();

        int selectedRoleId = binding.rgRole.getCheckedRadioButtonId();
        RadioButton selectedRole = findViewById(selectedRoleId);
        String role = selectedRole != null ? selectedRole.getText().toString() : "Patient";

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.error_fill_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("full_name", name);
            postData.put("email", email);
            postData.put("password", password);
            postData.put("role", role);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                REGISTER_URL,
                postData,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(this, R.string.registration_successful, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                            intent.putExtra("USER_EMAIL", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
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
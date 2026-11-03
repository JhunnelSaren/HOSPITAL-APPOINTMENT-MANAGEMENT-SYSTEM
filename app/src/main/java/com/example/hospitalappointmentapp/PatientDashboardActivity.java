package com.example.hospitalappointmentapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hospitalappointmentapp.databinding.ActivityPatientDashboardBinding;

import java.util.ArrayList;

public class PatientDashboardActivity extends AppCompatActivity {

    private ActivityPatientDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupUI();
    }

    private void setupUI() {
        binding.rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        
        // In a real app, you would fetch these from the backend
        // For now, let's show an empty state or some dummy data if you want
        binding.tvNoAppointments.setVisibility(View.VISIBLE);
        
        binding.fabBook.setOnClickListener(v -> {
            Toast.makeText(this, "Booking feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }
}
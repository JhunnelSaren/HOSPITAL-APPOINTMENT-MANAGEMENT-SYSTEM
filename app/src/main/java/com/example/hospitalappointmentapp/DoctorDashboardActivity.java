package com.example.hospitalappointmentapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.hospitalappointmentapp.databinding.ActivityDoctorDashboardBinding;

public class DoctorDashboardActivity extends AppCompatActivity {
    private ActivityDoctorDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvDoctorAppointments.setLayoutManager(new LinearLayoutManager(this));
        // Logic to load doctor-specific appointments would go here
    }
}
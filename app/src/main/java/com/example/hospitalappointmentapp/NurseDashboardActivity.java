package com.example.hospitalappointmentapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.hospitalappointmentapp.databinding.ActivityNurseDashboardBinding;

public class NurseDashboardActivity extends AppCompatActivity {
    private ActivityNurseDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNurseDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvNursePatients.setLayoutManager(new LinearLayoutManager(this));
        // Logic for nurse-specific view
    }
}
package com.rabbi.e_commercealibaba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rabbi.e_commercealibaba.databinding.ActivityConfirmFinalOrderBinding;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    ActivityConfirmFinalOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmFinalOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
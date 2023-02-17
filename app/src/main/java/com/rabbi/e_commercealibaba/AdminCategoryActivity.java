package com.rabbi.e_commercealibaba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rabbi.e_commercealibaba.databinding.ActivityAdminCategoryBinding;

public class AdminCategoryActivity extends AppCompatActivity {

    ActivityAdminCategoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);

        binding.tShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","tShirt");
                startActivity(intent);
            }
        });
        binding.sportTShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","sportTShirt");
                startActivity(intent);
            }
        });
        binding.femaleDress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","femaleDress");
                startActivity(intent);
            }
        });
        binding.sweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","sweather");
                startActivity(intent);
            }
        });

        binding.sunGlasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","sunGlasses");
                startActivity(intent);
            }
        });
        binding.pursesBags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","pursesBags");
                startActivity(intent);
            }
        });
        binding.hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","hats");
                startActivity(intent);
            }
        });
        binding.shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","shoes");
                startActivity(intent);
            }
        });

        binding.headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","headphones");
                startActivity(intent);
            }
        });
        binding.laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","laptops");
                startActivity(intent);
            }
        });
        binding.watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","watches");
                startActivity(intent);
            }
        });
        binding.mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category","mobiles");
                startActivity(intent);
            }
        });
    }
}
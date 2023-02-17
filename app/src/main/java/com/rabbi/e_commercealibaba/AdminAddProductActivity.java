package com.rabbi.e_commercealibaba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AdminAddProductActivity extends AppCompatActivity {

    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admic_add_product);

        categoryName = getIntent().getStringExtra("category").toString();

    }
}
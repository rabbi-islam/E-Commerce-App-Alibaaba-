package com.rabbi.e_commercealibaba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AdmicAddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admic_add_product);

        Toast.makeText(this, "Hi, From Admin", Toast.LENGTH_SHORT).show();
    }
}
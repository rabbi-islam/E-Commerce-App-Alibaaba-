package com.rabbi.e_commercealibaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rabbi.e_commercealibaba.Models.ProductData;
import com.rabbi.e_commercealibaba.databinding.ActivityProductDetailsBinding;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {

    ActivityProductDetailsBinding binding;
    String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productID = getIntent().getStringExtra("pid");
        
        getProductDetails(productID);
    }

    private void getProductDetails(String productID) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
        reference.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProductData data = snapshot.getValue(ProductData.class);
                    binding.productDetailsName.setText(data.getName());
                    binding.productDetailsDesc.setText(data.getDescription());
                    binding.productDetailsPrice.setText(data.getPrice());

                    try {
                        Picasso.get().load(data.getImage()).placeholder(R.drawable.profile).into(binding.productDetailsImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
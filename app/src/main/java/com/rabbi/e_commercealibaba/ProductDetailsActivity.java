package com.rabbi.e_commercealibaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rabbi.e_commercealibaba.Models.ProductData;
import com.rabbi.e_commercealibaba.Prevalent.Prevalent;
import com.rabbi.e_commercealibaba.databinding.ActivityProductDetailsBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {

    ActivityProductDetailsBinding binding;
    String productID = "";
    int totalQuantity = 1;
    String currentDate,currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productID = getIntent().getStringExtra("pid");

        
        getProductDetails(productID);



        binding.increaseIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalQuantity < 10 ){
                    totalQuantity++;
                    binding.productQuantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
        
        binding.decreaseIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity > 1 ){
                    totalQuantity--;
                    binding.productQuantity.setText(String.valueOf(totalQuantity));
                }
            }
        });

        binding.productQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                totalQuantity = Integer.parseInt(binding.productQuantity.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        
        binding.cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });
    }

    private void addToCart() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        currentDate = dateFormat.format(calendar.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        currentTime = timeFormat.format(calForTime.getTime());
       final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartList = new HashMap<>();
        cartList.put("pid",productID);
        cartList.put("pname",binding.productDetailsName.getText().toString());
        cartList.put("price",binding.productDetailsPrice.getText().toString());
        cartList.put("date",currentDate);
        cartList.put("time",currentTime);
        cartList.put("discount","");
        cartList.put("quantity",String.valueOf(totalQuantity));
        reference.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                 .child("Products").child(productID).updateChildren(cartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            reference.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID).updateChildren(cartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this, "product added to the cart", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ProductDetailsActivity.this,HomeActivity.class));
                                            }
                                        }
                                    });
                        }
                    }
                });


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
                Toast.makeText(ProductDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
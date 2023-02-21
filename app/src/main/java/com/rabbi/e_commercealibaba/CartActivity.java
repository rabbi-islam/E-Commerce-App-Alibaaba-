package com.rabbi.e_commercealibaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rabbi.e_commercealibaba.Adapter.CartAdapter;
import com.rabbi.e_commercealibaba.Models.CartData;
import com.rabbi.e_commercealibaba.Prevalent.Prevalent;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRecyclerView;
    CartAdapter adapter;
    List<CartData> list;
    private DatabaseReference reference;
    TextView priceTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        priceTv = findViewById(R.id.priceTV);

        String totalPrice = getIntent().getStringExtra("totalPrice");

        reference = FirebaseDatabase.getInstance().getReference().child("Cart List");
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        getData();
    }

    private void getData() {
        reference.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        list = new ArrayList<>();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            CartData cartData = dataSnapshot.getValue(CartData.class);
                            list.add(cartData);
                        }
                        adapter = new CartAdapter(list,CartActivity.this);
                        cartRecyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                        cartRecyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
package com.rabbi.e_commercealibaba;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rabbi.e_commercealibaba.Adapter.ProductAdapter;
import com.rabbi.e_commercealibaba.Models.ProductData;
import com.rabbi.e_commercealibaba.Prevalent.Prevalent;
import com.rabbi.e_commercealibaba.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private List<ProductData> productList;
    RecyclerView productRecyclerView;
    ProductAdapter productAdapter;
    private DatabaseReference reference;
    private ActivityHomeBinding binding;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        productRecyclerView = binding.productRecyclerView;
        productRecyclerView = findViewById(R.id.recyclerMenu);
        reference = FirebaseDatabase.getInstance().getReference().child("Products");
        getData();

        Paper.init(this);
        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         drawer = binding.drawerLayout;

         binding.navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,binding.appBarHome.toolbar,R.string.open_nav,R.string.close_nav);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View view = binding.navView.getHeaderView(0);
        TextView userNameTv = view.findViewById(R.id.userName);
        userNameTv.setText(Prevalent.currentOnlineUser.getName());
        CircleImageView userProfileImage = view.findViewById(R.id.userProfileImage);

    }

    private void getData() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                productList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ProductData data = dataSnapshot.getValue(ProductData.class);
                    productList.add(data);
                }

                productAdapter = new ProductAdapter(productList,HomeActivity.this);
                productRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                productRecyclerView.setAdapter(productAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.nav_cart){

        }else if (id== R.id.nav_categories){

        }else if (id== R.id.nav_orders){

        }else if (id== R.id.nav_setting){
            startActivity(new Intent(HomeActivity.this,SettingsActivity.class));

        }else if (id== R.id.nav_logout){
            Paper.book().destroy();
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}
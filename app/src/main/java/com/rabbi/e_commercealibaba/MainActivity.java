package com.rabbi.e_commercealibaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rabbi.e_commercealibaba.Models.Users;
import com.rabbi.e_commercealibaba.Prevalent.Prevalent;
import com.rabbi.e_commercealibaba.databinding.ActivityMainBinding;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference();
        Paper.init(this);
        pd = new ProgressDialog(this);

    binding.loginButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
    });


    binding.joinNowButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this,RegisterActivity.class));
        }
    });

    String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
    String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);

    if (userPhoneKey != "" && userPasswordKey != ""){
        if (!TextUtils.isEmpty(userPhoneKey) &&  !TextUtils.isEmpty(userPasswordKey) ){
            allowAccess(userPhoneKey,userPasswordKey);
            pd.setTitle("Checking!");
            pd.setMessage("Already logged in!");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
    }

    }

    private void allowAccess(String phone, String password) {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("Users").child(phone).exists()){
                    Users userData = snapshot.child("Users").child(phone).getValue(Users.class);

                    assert userData != null;
                    if (userData.getPhone().equals(phone)){
                        if (userData.getPassword().equals(password)){
                            pd.dismiss();
                            Prevalent.currentOnlineUser = userData;
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }else{
                            Toast.makeText(MainActivity.this, "wrong password!", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Credential's didn't matched", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }


                }else{
                    Toast.makeText(MainActivity.this, "Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
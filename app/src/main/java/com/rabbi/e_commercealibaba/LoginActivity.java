package com.rabbi.e_commercealibaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rabbi.e_commercealibaba.Models.Users;
import com.rabbi.e_commercealibaba.Prevalent.Prevalent;
import com.rabbi.e_commercealibaba.databinding.ActivityLoginBinding;
import com.rabbi.e_commercealibaba.databinding.ActivityMainBinding;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    ProgressDialog pd;
    DatabaseReference reference;
    String parentDB = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference();
        pd = new ProgressDialog(this);
        Paper.init(this);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


    }

    private void loginUser() {

        String phone = binding.phoneNumber.getText().toString();
        String password = binding.password.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "phone must not be empty!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "password number must not be empty!", Toast.LENGTH_SHORT).show();
        }else{
            pd.setTitle("Login Account!");
            pd.setMessage("We're checking your credential's please wait");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            allowAccessToAccount(phone,password);


        }
    }

    private void allowAccessToAccount(String phone, String password) {

        if (binding.checkbox.isChecked()){
            Paper.book().write(Prevalent.userPhoneKey,phone);
            Paper.book().write(Prevalent.userPasswordKey,password);
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(parentDB).child(phone).exists()){
                    Users userData = snapshot.child(parentDB).child(phone).getValue(Users.class);

                    assert userData != null;
                    if (userData.getPhone().equals(phone)){
                        if (userData.getPassword().equals(password)){
                            Toast.makeText(LoginActivity.this, "logged in successfully!", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this, "wrong password!", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "Credential's didn't matched", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }


                }else{
                    Toast.makeText(LoginActivity.this, "Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
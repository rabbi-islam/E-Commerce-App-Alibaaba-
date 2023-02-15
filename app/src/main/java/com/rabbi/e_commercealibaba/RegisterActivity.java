package com.rabbi.e_commercealibaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rabbi.e_commercealibaba.databinding.ActivityRegisterBinding;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    ProgressDialog pd;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pd = new ProgressDialog(this);
        reference = FirebaseDatabase.getInstance().getReference();

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name = binding.name.getText().toString();
        String phone = binding.phoneNumber.getText().toString();
        String password = binding.password.getText().toString();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "name must not be empty!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "phone number must not be empty!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "password must not be empty!", Toast.LENGTH_SHORT).show();
        }else{
            pd.setTitle("Creating Account!");
            pd.setMessage("We're creating your account please wait");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            validatePhoneNumber(name,phone,password);


        }
    }

    private void validatePhoneNumber(String name, String phone, String password) {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phone).exists())){

                    HashMap<String,Object> users = new HashMap<>();
                    users.put("name",name);
                    users.put("phone",phone);
                    users.put("password",password);
                    reference.child("Users").child(phone).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "account created successfully", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }else{
                                Toast.makeText(RegisterActivity.this, "please try again", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }

                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this, "phone number already exist", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.rabbi.e_commercealibaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.rabbi.e_commercealibaba.Prevalent.Prevalent;
import com.rabbi.e_commercealibaba.databinding.ActivitySettingsBinding;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    DatabaseReference reference;
    private Uri imageUri;
    private  String myUrl="";
    StorageReference profilePicRef;
    private  String checker ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        userInfoDisplay(binding.updateProfileImage,binding.name,binding.phoneNumber,binding.address);

        binding.closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.updateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")){
                    userInfoSaved();
                }else{
                    updateOnlyUserinfo();
                }
            }
        });

        binding.changeProfileTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker= "clicked";
            }
        });
    }

    private void updateOnlyUserinfo() {
    }

    private void userInfoSaved() {
    }

    private void userInfoDisplay(CircleImageView updateProfileImage, EditText name, EditText phoneNumber, EditText address) {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("image").exists()){
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(binding.updateProfileImage);
                        binding.name.setText(name);
                        binding.phoneNumber.setText(phone);
                        binding.address.setText(address);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
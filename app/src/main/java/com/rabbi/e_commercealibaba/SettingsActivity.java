package com.rabbi.e_commercealibaba;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rabbi.e_commercealibaba.Prevalent.Prevalent;
import com.rabbi.e_commercealibaba.databinding.ActivitySettingsBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    DatabaseReference reference;
    private Uri imageUri;
    private  String myUrl="";
    StorageReference profilePicRef;
    private ProgressDialog pd;
    private  String checker ="";
    ActivityResultLauncher<String> resultLauncher;
    StorageTask uploadTask;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         storageReference = FirebaseStorage.getInstance().getReference("Product Images");

        resultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                if (result != null) {

                    Intent intent = new Intent(SettingsActivity.this, CropperActivity.class);
                    intent.putExtra("DATA", result.toString());
                    startActivityForResult(intent, 101);
                }
                else{
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                }
            }
        });
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        pd = new ProgressDialog(this);
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
                resultLauncher.launch("image/*");
            }
        });
    }

    private void updateOnlyUserinfo() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> map = new HashMap<>();
        map.put("name",binding.name.getText().toString());
        map.put("address",binding.address.getText().toString());
        map.put("phone",binding.phoneNumber.getText().toString());

        databaseReference.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(map);
        databaseReference.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(map);
        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        finish();

    }

    private void userInfoSaved() {

        if (TextUtils.isEmpty(binding.name.getText().toString())){
            Toast.makeText(this, "name must not be empty", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(binding.phoneNumber.getText().toString())){
            Toast.makeText(this, "number must not be empty", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(binding.address.getText().toString())){
            Toast.makeText(this, "address must not be empty", Toast.LENGTH_SHORT).show();
        }else if (checker.equals("clicked")){
            uploadImage();
        }
    }

    private void uploadImage() {
        pd.setTitle("uploading!");
        pd.setMessage("We're uploading image, please wait");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        if (imageUri != null){
            StorageReference fileRef = storageReference.child(Prevalent.currentOnlineUser.getPhone()+ " .jpg");
            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful())
                        throw  task.getException();
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("name",binding.name.getText().toString());
                        map.put("address",binding.address.getText().toString());
                        map.put("phone",binding.phoneNumber.getText().toString());
                        map.put("image",myUrl);
                        databaseReference.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(map);
                        pd.dismiss();
                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        finish();
                    }else {
                        pd.dismiss();
                        Toast.makeText(SettingsActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "image not selected", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }

   


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
                Toast.makeText(SettingsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1 && requestCode == 101 && data  != null){

            String result = data.getStringExtra("RESULT");
            imageUri  = null;

            if (result != null){
                imageUri = Uri.parse(result);
            }

            binding.updateProfileImage.setImageURI(imageUri);

        }else{

        }
    }
}
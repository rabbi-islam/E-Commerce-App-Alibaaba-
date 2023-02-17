package com.rabbi.e_commercealibaba;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rabbi.e_commercealibaba.databinding.ActivityAdmicAddProductBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AdminAddProductActivity extends AppCompatActivity {

    ActivityAdmicAddProductBinding binding;
    String productName,productDesc,productPrice;
    private ProgressDialog pd;
    String categoryName;
    private final int REQ = 1;
    Uri imageUri;
    private Bitmap bitmap;
    String currentDate,currentTime,productRandomKey,downloadImageUrl;
    StorageReference productImageRef;
    DatabaseReference productDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdmicAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");
        pd = new ProgressDialog(this);

        categoryName = getIntent().getStringExtra("category");
        binding.selectedProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        binding.addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateProductData();
            }
        });

    }

    private void validateProductData() {

        productName = binding.productName.getText().toString();
        productDesc = binding.productDesc.getText().toString();
        productPrice = binding.productPrice.getText().toString();

        if (imageUri==null){
            Toast.makeText(this, "product image required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(productName)){
            Toast.makeText(this, "product name required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(productDesc)){
            Toast.makeText(this, "product description required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(productPrice)){
            Toast.makeText(this, "product price required", Toast.LENGTH_SHORT).show();
        }else{
            storeProductInformation();
        }
    }

    private void storeProductInformation() {

        pd.setTitle("Add New Product");
        pd.setMessage("we're adding a new product please wait");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        currentDate = dateFormat.format(calendar.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        currentTime = timeFormat.format(calForTime.getTime());

        productRandomKey = currentDate+currentTime;
        StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddProductActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> taskUrl = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw Objects.requireNonNull(task.getException());
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                       if (task.isSuccessful()){
                           downloadImageUrl  = task.getResult().toString();
                           Toast.makeText(AdminAddProductActivity.this, "image retrieved in database from storage", Toast.LENGTH_SHORT).show();
                           saveProductInfoToDatabase();
                       }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminAddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void saveProductInfoToDatabase() {

        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",currentDate);
        productMap.put("time",currentTime);
        productMap.put("image",downloadImageUrl);
        productMap.put("name",productName);
        productMap.put("description",productDesc);
        productMap.put("price",productPrice);
        productMap.put("category",categoryName);
        productDatabaseReference.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(AdminAddProductActivity.this, "product added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminAddProductActivity.this,AdminCategoryActivity.class));
                }else{
                    pd.dismiss();
                    Toast.makeText(AdminAddProductActivity.this, task.getException()+ "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ && resultCode == RESULT_OK && data!=null) {
             imageUri = data.getData();
            binding.selectedProductImage.setImageURI(imageUri);
        }
    }
}
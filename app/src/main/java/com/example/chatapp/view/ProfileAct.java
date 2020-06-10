package com.example.chatapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityProfileBinding;
import com.example.chatapp.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileAct extends AppCompatActivity {
    ActivityProfileBinding binding;

    FirebaseUser auth;
    DatabaseReference reference;
    private String userID;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST=1;
    private Uri imageUri;
    private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        auth=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
        loadUserProfile();
        binding.addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
    }
    private void loadUserProfile(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                binding.userName.setText(user.getUsername());
                binding.tvEmail.setText(user.getEmail());
                if (user.getAddress().equals("0") || user.getBio().equals("0") ||
                        user.getPhonenumber().equals("0") || user.getImageUrl().equals("default")){
                    binding.tvAddres.setText("tambahkan alamat");
                    binding.tvPhonenumber.setText("tambahkan nomber telepon");
                    binding.tvBio.setText("tambahkan bio");
                    binding.imgProfile.setImageResource(R.drawable.no_pic);
                }else {
                    binding.tvAddres.setText(user.getAddress());
                    binding.tvPhonenumber.setText(user.getPhonenumber());
                    binding.tvBio.setText(user.getBio());
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(binding.imgProfile);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void openImage(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=ProfileAct.this.getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog progressDialog=new ProgressDialog(ProfileAct.this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (imageUri != null){
            final  StorageReference fileReference=storageReference.child(System.currentTimeMillis()+"."
                    +getFileExtension(imageUri));
            uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }else {
                        return fileReference.getDownloadUrl();
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Log.d("jalan1","oke");
                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();
                        reference=FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
                        HashMap<String, Object> hashMap=new HashMap<>();
                        hashMap.put("imageUrl",mUri);
                        reference.updateChildren(hashMap);
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(ProfileAct.this,"Failed!",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileAct.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }else {
            Toast.makeText(ProfileAct.this,"No image selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST
                && data != null && data.getData() != null){
            imageUri=data.getData();
            Log.d("imageuri","berhasil");
            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(ProfileAct.this,"Uploading in progress",Toast.LENGTH_LONG).show();
            }else {
                uploadImage();
            }
        }
    }
}

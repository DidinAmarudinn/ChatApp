package com.example.chatapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.databinding.ActivityRegisterBinding;
import com.example.chatapp.helper.PrefHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterAct extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference reference;
    PrefHelper prefHelper;
    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefHelper=new PrefHelper(this);
        auth=FirebaseAuth.getInstance();
        binding.brnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.etEmail.getText().toString();
                String username=binding.etUsername.getText().toString();
                String password=binding.etPassword.getText().toString();
                String bio="0";
                String phone_number="0";
                String address="0";
                if (TextUtils.isEmpty(email)|| TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterAct.this,"Mohon isi semua field",Toast.LENGTH_LONG).show();
                }else if(password.length() < 6 ){
                    Toast.makeText(RegisterAct.this,"Passwor harus lebih dari 6 chacrter",Toast.LENGTH_LONG).show();
                }else {
                    register(email,password,username,bio,phone_number,address);
                }
            }
        });
        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterAct.this,LoginAct.class);
                startActivity(intent);
            }
        });
    }
    private void register(String email, String password, String username,String bio, String phonenumber, String addres){
        binding.progressbar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.progressbar.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userID=firebaseUser.getUid();
                            reference=FirebaseDatabase.getInstance().getReference("Users").child(userID);
                            HashMap<String, String> hashMap=new HashMap<>();
                            hashMap.put("id",userID);
                            hashMap.put("username", username);
                            hashMap.put("email",email);
                            hashMap.put("phonenumber",phonenumber);
                            hashMap.put("address",addres);
                            hashMap.put("bio",bio);
                            hashMap.put("imageUrl", "default");
                            hashMap.put("status","offline");
                            hashMap.put("search",username.toLowerCase());
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        prefHelper.putUID(userID);
                                        prefHelper.putUsername(username);
                                        Intent intent=new Intent(RegisterAct.this,HomeAct.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else {
                            binding.progressbar.setVisibility(View.GONE);
                            Toast.makeText(RegisterAct.this,"Email sudah pernah digunakan",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

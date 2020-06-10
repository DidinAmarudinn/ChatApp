package com.example.chatapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginAct extends AppCompatActivity {
    private ActivityLoginBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.etEmail.getText().toString();
                String password=binding.etPassword.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginAct.this,"Mohon isi semua field",Toast.LENGTH_LONG).show();
                }else {
                    login(email,password);
                }
            }
        });
    }
    public void login(String email,String password){
        binding.progressbar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.progressbar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Intent intent =new Intent(LoginAct.this,HomeAct.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else {
                            binding.progressbar.setVisibility(View.GONE);
                            Toast.makeText(LoginAct.this,"Email atau Password salah",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

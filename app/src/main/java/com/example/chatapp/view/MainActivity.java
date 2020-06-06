package com.example.chatapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    FirebaseUser auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance().getCurrentUser();
        binding.btnMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starAct(RegisterAct.class);
            }
        });

    }

    private void starAct(Class clas){
        Intent intent=new Intent(MainActivity.this,clas);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth != null){
            starAct(HomeAct.class);
        }
    }
}

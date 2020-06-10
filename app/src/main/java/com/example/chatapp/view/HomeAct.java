package com.example.chatapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.adapter.ViewPagerAdapter;
import com.example.chatapp.databinding.ActivityHomeBinding;
import com.example.chatapp.fragment.ChatFragment;
import com.example.chatapp.fragment.UserFragment;
import com.example.chatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HomeAct extends AppCompatActivity {

    FirebaseUser auth;
    private String userId;
    private ActivityHomeBinding binding;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("");
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
        loadProfile();

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),2);
        viewPagerAdapter.addFragment(new ChatFragment(),"Chat");
        viewPagerAdapter.addFragment(new UserFragment(),"User");
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.toProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProfile();
            }
        });
    }
    private void startAct(Class classt){
        Intent intent=new Intent(HomeAct.this,classt);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (auth == null){
            startAct(RegisterAct.class);
        }
    }


    private void loadProfile(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                userId=user.getId();
                binding.tvUsername.setText(user.getUsername());
                if (user.getImageUrl().equals("default")){
                    binding.imgProfile.setImageResource(R.drawable.no_pic);
                }else {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(binding.imgProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeAct.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
        }
        return false;
    }
    private void toProfile(){
        Intent intent=new Intent(HomeAct.this,ProfileAct.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }
    private void status(String status){
        reference=FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        status("online");
    }
    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}

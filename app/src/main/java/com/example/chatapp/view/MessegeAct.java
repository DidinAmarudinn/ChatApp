package com.example.chatapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.adapter.MessageAdapter;
import com.example.chatapp.databinding.ActivityMessegeBinding;
import com.example.chatapp.helper.PrefHelper;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class MessegeAct extends AppCompatActivity {
    private Calendar calendar;

    private DatabaseReference reference;
    PrefHelper prefHelper;
    FirebaseUser muser;
    private String userID;
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    private String username;
    private ActivityMessegeBinding binding;
    private ValueEventListener seenListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMessegeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        prefHelper=new PrefHelper(this);
        getSupportActionBar().setTitle("");
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessegeAct.this,HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        binding.rvMessage.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMessage.setHasFixedSize(true);
        userID=getIntent().getStringExtra("userID");
        muser= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Users").child(userID);
        loadDataUser();

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMessage();
            }
        });
        seenMessage(userID);
    }
    private void loadDataUser(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user= dataSnapshot.getValue(User.class);
                username=user.getUsername();
                binding.tvUsername.setText(user.getUsername());
                Log.d("usernmae",user.getUsername());
                if (user.getImageUrl().equals("default")){
                    binding.imgProfile.setImageResource(R.drawable.no_pic);
                }else {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(binding.imgProfile);
                }
                readMessage(muser.getUid(),userID);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void readMessage(final String myId, final String userid){
        mchat=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if (chat.getReciver().equals(myId) && chat.getSender().equals(userid)
                            || chat.getReciver().equals(userid) && chat.getSender().equals(myId)){
                        mchat.add(chat);
                    }
                    messageAdapter=new MessageAdapter(mchat,MessegeAct.this);
                    binding.rvMessage.setAdapter(messageAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender, String reciver, String message, String time, String tgl,String sender_name){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String, String> hashMap=new HashMap<>();
        hashMap.put("sender_name",sender_name);
        hashMap.put("sender",sender);
        hashMap.put("reciver",reciver);
        hashMap.put("message",message);
        hashMap.put("time",time);
        hashMap.put("tgl", tgl);
        reference.child("Chats").push().setValue(hashMap);
    }
    private void setMessage(){
        String message=binding.etMessage.getText().toString();
        calendar=Calendar.getInstance();
        SimpleDateFormat dateTgl=new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeNow=new SimpleDateFormat("h:mm a");
        String dateNow=timeNow.format(calendar.getTime());
        String tglNow=dateTgl.format(calendar.getTime());
        if (!message.isEmpty()){
            sendMessage(muser.getUid(),userID,message,dateNow,tglNow,prefHelper.getUsername());
        }else{
            Toast.makeText(MessegeAct.this,"Tidak dapat mengirim pesan kosong",Toast.LENGTH_SHORT).show();
        }
        binding.etMessage.setText("");
    }

    private void status(String status){
        reference=FirebaseDatabase.getInstance().getReference("Users").child(muser.getUid());
        HashMap<String ,Object> hashMap=new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }
    private void seenMessage(String userid){
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if (chat.getReciver().equals(muser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
    }
}

package com.example.chatapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.adapter.UserAdapter;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private UserAdapter userAdapter;
    private RecyclerView rv_chat;
    private List<User> muser;
    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> userList;
    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        rv_chat=view.findViewById(R.id.rv_chat);
        rv_chat.setHasFixedSize(true);
        rv_chat.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        userList=new ArrayList<>();
        checkUserChat();
        return view;
    }

    private void checkUserChat(){
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat=snapshot.getValue(Chat.class);
                    if (chat.getSender().equals(fuser.getUid())){
                        userList.add(chat.getReciver());
                    }
                    if (chat.getReciver().equals(fuser.getUid())){
                        userList.add(chat.getSender());
                    }
                }
                readChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readChat() {
        muser=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                muser.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    for (String id : userList){
                        if (user.getId().equals(id)){
                            if (muser.size()>0){
                                for (User user1: muser){
                                    if (!user.getId().equals(user1.getId())){
                                        muser.add(user);
                                    }
                                }
                            }else {
                                muser.add(user);
                            }
                        }
                    }
                }
                userAdapter=new UserAdapter(muser,getContext(),true);
                rv_chat.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

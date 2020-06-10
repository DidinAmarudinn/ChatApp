package com.example.chatapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.adapter.UserAdapter;
import com.example.chatapp.model.User;
import com.example.chatapp.view.HomeAct;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.INPUT_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class UserFragment extends Fragment {
    private RecyclerView rv_user;
    private UserAdapter userAdapter;
    private RelativeLayout relativ_notfound;
    private ImageView btn_back;
    private LinearLayout linearLayoutSearch;
    private List<User> list;
    private FloatingActionButton fab;
    private EditText et_search_user;
    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);
        rv_user=view.findViewById(R.id.rv_user);
        rv_user.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_user.setHasFixedSize(true);
        fab=view.findViewById(R.id.fab);
        et_search_user=view.findViewById(R.id.et_search_user);
        btn_back=view.findViewById(R.id.btn_back1);
        linearLayoutSearch=view.findViewById(R.id.linear);
        relativ_notfound=view.findViewById(R.id.relativ_notfound);
        list=new ArrayList<>();
        readUser();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSearch.setVisibility(View.VISIBLE);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSearch.setVisibility(View.GONE);
            }
        });
        et_search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchUser(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void readUser() {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Log.d("firebaseuserid",firebaseUser.getUid());
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (et_search_user.getText().toString().isEmpty()) {
                list.clear();
                for (DataSnapshot mdatasnap : dataSnapshot.getChildren()){
                    User user=mdatasnap.getValue(User.class);
                    Log.d("userid",user.getId());
                    assert  user!=null;
                    assert firebaseUser!=null;
                    if (dataSnapshot.exists()){
                        if (!user.getId().equals(firebaseUser.getUid())){
                            list.add(user);
                        }
                    }
                }}
                    userAdapter=new UserAdapter(list,getContext(),false);
                    rv_user.setAdapter(userAdapter);
                if (userAdapter.getItemCount() == 0){
                    relativ_notfound.setVisibility(View.VISIBLE);
                }else {
                    relativ_notfound.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void searchUser(String s) {
        FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        assert fuser != null;
                        assert user != null;
                        if (!user.getId().equals(fuser.getUid())) {
                            list.add(user);
                        }

                    }
                    userAdapter = new UserAdapter(list, getContext(), false);
                    rv_user.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

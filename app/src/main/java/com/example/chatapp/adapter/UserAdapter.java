package com.example.chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ListChatItemBinding;
import com.example.chatapp.databinding.ListItemUserBinding;
import com.example.chatapp.model.User;
import com.example.chatapp.view.MessegeAct;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> list;
    private Context context;
    private boolean isChat;

    public UserAdapter(List<User> list, Context context,boolean isChat) {
        this.list = list;
        this.context = context;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.list_item_user,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user=list.get(position);
        holder.tv_list_user.setText(user.getUsername());
        if (user.getImageUrl().equals("default")){
            holder.imguser.setImageResource(R.drawable.no_pic);
        }else {
            Glide.with(context).load(user.getImageUrl()).into(holder.imguser);
        }
        if (isChat){
            if (user.getStatus().equals("online")){
                holder.txtOnline.setText("Online");
            }else {
                holder.txtOnline.setText("Offline");
                holder.txtOnline.setTextColor(Color.parseColor("#BFBFBF"));
            }
        }
        else {
            holder.txtOnline.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MessegeAct.class);
                intent.putExtra("userID",user.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_list_user;
        private ImageView imguser;
        private TextView txtOnline;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_list_user=itemView.findViewById(R.id.tv_list_user_user);
            imguser=itemView.findViewById(R.id.img_user);
            txtOnline=itemView.findViewById(R.id.txtOnline);

        }
    }
}

package com.example.chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.example.chatapp.view.MessegeAct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private List<Chat> list;
    private Context context;
    FirebaseUser fuser;
    
    public MessageAdapter(List<Chat> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_chat_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.list_chat_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat=list.get(position);
        holder.tv_show_message.setText(chat.getMessage());
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat dateTgl=new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeNow=new SimpleDateFormat("h:mm a");
        String time=timeNow.format(calendar.getTime());
        String tglNow=dateTgl.format(calendar.getTime());
        if (chat.getTgl().equals(tglNow)){
            holder.tv_time.setText(chat.getTime());
        }else {
            holder.tv_time.setText(chat.getTgl());
        }
        if (chat.getSender().equals(fuser.getUid())){
            holder.tv_user_chat.setText("me");
        }else {
            holder.tv_user_chat.setText(chat.getSender_name());
        }


        if (position == list.size()-1){
            if (chat.isseen){
                holder.txt_seen.setText("seen");
            }else {
                holder.txt_seen.setTextColor(Color.parseColor("#BFBFBF"));
                holder.txt_seen.setText("delivered");
            }
        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_show_message;
        private TextView tv_time;
        private TextView tv_user_chat;
        private TextView txt_seen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_show_message=itemView.findViewById(R.id.tv_show_message);
            tv_time=itemView.findViewById(R.id.tv_time);
            txt_seen=itemView.findViewById(R.id.txt_seen);
            tv_user_chat=itemView.findViewById(R.id.tv_user_chat);
           }
    }
    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}


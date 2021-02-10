package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Activities.ChattingActivity;
import com.example.whatsappclone.ModalClasses.User;
import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.RecyclerviewChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    Context context;
    ArrayList<User> users;

    public UserAdapter(Context context , ArrayList<User> user)
    {
        this.context = context;
        this.users = user;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_chats,parent,false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User userInfo = users.get(position);

        holder.recyclerviewChatsBinding.tvUserName.setText(userInfo.getName());
        Glide.with(context).load(userInfo.getProfileImage())
                .placeholder(R.drawable.user)
                .into(holder.recyclerviewChatsBinding.ivUserImage);

        String senderID = FirebaseAuth.getInstance().getUid();
        String senderRoom = senderID + userInfo.getUid();

        FirebaseDatabase.getInstance().getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            String lastMsg = snapshot.child("LastMsg").getValue(String.class);
                            Long time = snapshot.child("LastMsgTime").getValue(Long.class);

                            holder.recyclerviewChatsBinding.tvLastMessage.setText(lastMsg);

                        }else {
                            holder.recyclerviewChatsBinding.tvLastMessage.setText("Tap to chat...");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChattingActivity.class);
                intent.putExtra("name",userInfo.getName());
                intent.putExtra("uid",userInfo.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }



    public class UserViewHolder extends RecyclerView.ViewHolder{

        RecyclerviewChatsBinding recyclerviewChatsBinding;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerviewChatsBinding = RecyclerviewChatsBinding.bind(itemView);
        }
    }
}

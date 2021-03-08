package com.example.whatsappclone.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.whatsappclone.Adapters.MessageAdapter;
import com.example.whatsappclone.ModalClasses.Message;
import com.example.whatsappclone.databinding.ActivityChattingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChattingActivity extends AppCompatActivity {

    ActivityChattingBinding activityChattingBinding;

    MessageAdapter adapter;
    ArrayList<Message> messages;

    String senderRoom;
    String receiverRoom;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChattingBinding = ActivityChattingBinding.inflate(getLayoutInflater());
        setContentView(activityChattingBinding.getRoot());

        messages = new ArrayList<Message>();
        adapter = new MessageAdapter(this,messages);
        activityChattingBinding.recyclerViewChatting.setLayoutManager(new LinearLayoutManager(this));
        activityChattingBinding.recyclerViewChatting.setAdapter(adapter);

        String name = getIntent().getStringExtra("name");
        String receiverUid = getIntent().getStringExtra("uid");
        String senderUid = FirebaseAuth.getInstance().getUid();
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //button for back button on top left corner

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        database = FirebaseDatabase.getInstance();

        database.getReference().child("Chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            Message message = snapshot1.getValue(Message.class);
                            messages.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        activityChattingBinding.btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageBoxText = activityChattingBinding.etMessageBox.getText().toString().trim();

                Date date = new Date();
                Message message = new Message(messageBoxText,senderUid,date.getTime());
                activityChattingBinding.etMessageBox.setText("");

                String randomKey = database.getReference().push().getKey();

                HashMap<String,Object> lastMessageObj = new HashMap<>();
                lastMessageObj.put("LastMsg" , message.getMessage());
                lastMessageObj.put("LastMsgTime" , date.getTime());
                database.getReference().child("Chats").child(senderRoom).updateChildren(lastMessageObj);
                database.getReference().child("Chats").child(receiverRoom).updateChildren(lastMessageObj);

                database.getReference().child("Chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomKey)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("Chats")
                                .child(receiverRoom)
                                .child("messages")
                                .child(randomKey)
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });


                    }
                });


            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() { //this is for back button to go back on the activity
        finish();
        return super.onSupportNavigateUp();
    }
}
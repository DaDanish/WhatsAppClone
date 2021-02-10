package com.example.whatsappclone.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsappclone.Adapters.UserAdapter;
import com.example.whatsappclone.ModalClasses.User;
import com.example.whatsappclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FragChats extends Fragment {

    FirebaseDatabase firebaseDatabase;
    ArrayList<User> userArrayList;
    UserAdapter userAdapter;

    RecyclerView recyclerviewChats;
    RecyclerView.LayoutManager layoutManager;



    View view;



    public FragChats() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_chats, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        userArrayList = new ArrayList<User>();

        recyclerviewChats = view.findViewById(R.id.recyclerviewChats);
        recyclerviewChats.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerviewChats.setLayoutManager(layoutManager);

        userAdapter = new UserAdapter(this.getContext(),userArrayList);
        recyclerviewChats.setAdapter(userAdapter);

        firebaseDatabase.getReference().child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userArrayList.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
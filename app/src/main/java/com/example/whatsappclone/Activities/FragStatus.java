package com.example.whatsappclone.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.whatsappclone.Adapters.StatusAdapter;
import com.example.whatsappclone.ModalClasses.Status;
import com.example.whatsappclone.ModalClasses.User;
import com.example.whatsappclone.ModalClasses.UserStatus;
import com.example.whatsappclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class FragStatus extends Fragment {

    StatusAdapter adapter;
    ArrayList <UserStatus> userStatuses;

    RecyclerView recyclerviewStatus;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;

    FloatingActionButton fabAdd;
    ProgressDialog dialog;
    View view;

    User user;

    public FragStatus(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_status, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userStatuses = new ArrayList<>();

        dialog = new ProgressDialog(this.getContext());
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();

        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("Stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot storySnapshot1: snapshot.getChildren()){
                        UserStatus status  = new UserStatus();
                        status.setName(storySnapshot1.child("name").getValue(String.class));
                        status.setProfileImage(storySnapshot1.child("profileImage").getValue(String.class));
                        status.setLastUpdated(storySnapshot1.child("lastUpdated").getValue(Long.class));

                        ArrayList<Status> statuses = new ArrayList<>();

                        for (DataSnapshot statusSnapshot : storySnapshot1.child("statuses").getChildren()){
                            Status sampleStatus = statusSnapshot.getValue(Status.class);
                            statuses.add(sampleStatus);
                        }
                        status.setStatuses(statuses);
                        userStatuses.add(status);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        fabAdd = view.findViewById(R.id.fabAdd);
        recyclerviewStatus = view.findViewById(R.id.recyclerviewStatus);


       //layoutManager = new LinearLayoutManager(this.getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerviewStatus.setLayoutManager(layoutManager);

        adapter = new StatusAdapter(this.getContext(),userStatuses);
        recyclerviewStatus.setAdapter(adapter);


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,123);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null)
        {
            if (data.getData() != null)
            {
                dialog.show();
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                Date date = new Date();
                StorageReference storageReference = firebaseStorage.getReference().child("Status").child(date.getTime()+"");
                storageReference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserStatus userStatus = new UserStatus();
                                    userStatus.setName(user.getName());
                                    userStatus.setProfileImage(user.getProfileImage());
                                    userStatus.setLastUpdated(date.getTime());

                                    HashMap<String,Object> obj = new HashMap<>();
                                    obj.put("name",userStatus.getName());
                                    obj.put("profileImage",userStatus.getProfileImage());
                                    obj.put("lastUpdated",userStatus.getLastUpdated());

                                    String imageURl = uri.toString();
                                    Status status = new Status(imageURl,userStatus.getLastUpdated());

                                    database.getReference().child("Stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);

                                    database.getReference().child("Stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("statuses")
                                            .push()
                                            .setValue(status);



                                    dialog.dismiss();

                                }
                            });
                        }
                    }
                });
            }
        }
    }
}
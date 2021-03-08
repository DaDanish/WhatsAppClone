package com.example.whatsappclone.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.ModalClasses.User;
import com.example.whatsappclone.databinding.ActivitySetupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetupActivity extends AppCompatActivity {

    String toCheckUpload;

    final int SETUP_CODE = 10;

    ActivitySetupBinding activitySetupBinding;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    Uri selectedImage;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySetupBinding = ActivitySetupBinding.inflate(getLayoutInflater());
        setContentView(activitySetupBinding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Profile...");
        progressDialog.setCancelable(false);

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        activitySetupBinding.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SETUP_CODE);

            }
        });


        activitySetupBinding.btnSetupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameInput = activitySetupBinding.etSetupName.getText().toString().trim();
                if (nameInput.isEmpty()) {
                    activitySetupBinding.etSetupName.setError("Field can't be empty");
                    return;
                }

                progressDialog.show();

                if (selectedImage != null)
                {
                    StorageReference storageReference = firebaseStorage.getReference().child("Profiles").child(auth.getUid());
                    storageReference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            String uID = auth.getUid();
                                            String phone = auth.getCurrentUser().getPhoneNumber();
                                            String name = activitySetupBinding.etSetupName.getText().toString().trim();

                                            User user = new User( uID , name , phone , imageUrl );

                                            firebaseDatabase.getReference().child("User")
                                                    .child(uID)
                                                    .setValue(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            Toast.makeText(SetupActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                            Intent intent = new Intent(SetupActivity.this,MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });

                                        }
                                    });
                                }
                                else
                                {
                                    Toast.makeText(SetupActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                    String uID = auth.getUid();
                    String phone = auth.getCurrentUser().getPhoneNumber();
                    String name = activitySetupBinding.etSetupName.getText().toString().trim();


                    User user = new User( uID , name.substring(0, 1).toUpperCase(), phone , "No image");

                    firebaseDatabase.getReference().child("User")
                            .child(uID)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(SetupActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(SetupActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }





            }
        });








    }

    private boolean validateName() {
        String nameInput = activitySetupBinding.etSetupName.getText().toString().trim();
        if (nameInput.isEmpty()) {
            activitySetupBinding.etSetupName.setError("Field can't be empty");
            return false;
        } else {
            activitySetupBinding.etSetupName.setError(null);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data !=null)
        {
            if (data.getData() != null)
            {
                if (requestCode == SETUP_CODE)
                {
                    activitySetupBinding.ivProfileImage.setImageURI(data.getData());
                    selectedImage = data.getData();
                }
            }
        }

    }
}
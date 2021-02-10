package com.example.whatsappclone.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsappclone.databinding.ActivityVerifyPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyPhoneNumber extends AppCompatActivity {

    ActivityVerifyPhoneNumberBinding activityVerifyPhoneNumberBinding;
    FirebaseAuth firebaseAuthForCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVerifyPhoneNumberBinding = ActivityVerifyPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(activityVerifyPhoneNumberBinding.getRoot());

        firebaseAuthForCheck = FirebaseAuth.getInstance();

        if (firebaseAuthForCheck.getCurrentUser() != null)
        {
            startActivity(new Intent(VerifyPhoneNumber.this,MainActivity.class));
            finish();
        }

        getSupportActionBar().hide();

        activityVerifyPhoneNumberBinding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifyPhoneNumber.this,OTP.class);
                intent.putExtra("phoneNumber",activityVerifyPhoneNumberBinding.etNumber.getText().toString().trim());
                startActivity(intent);
            }
        });





    }
}
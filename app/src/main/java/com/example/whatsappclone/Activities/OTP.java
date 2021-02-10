package com.example.whatsappclone.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.whatsappclone.databinding.ActivityOTPBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;

import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {

    ActivityOTPBinding activityOTPBinding;
    FirebaseAuth firebaseAuth;

    String verificationNumber;

    ProgressDialog progressDialog; //we use ProgressDialog to show loading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOTPBinding = ActivityOTPBinding.inflate(getLayoutInflater());
        setContentView(activityOTPBinding.getRoot());

        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending OTP...");
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();

        String num = getIntent().getStringExtra("phoneNumber");
        activityOTPBinding.tvVerifyNumber.setText("Verify " + num);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(num)
                .setTimeout(100l, TimeUnit.SECONDS)
                .setActivity(OTP.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationNumber = s;
                        progressDialog.dismiss();

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                        activityOTPBinding.otpView.requestFocus();

                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

        activityOTPBinding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationNumber,otp);

                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(OTP.this,SetupActivity.class));
                            finishAffinity();   //this will finish all the  previous activities
                        }
                        else {
                            Toast.makeText(OTP.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });






    }
}
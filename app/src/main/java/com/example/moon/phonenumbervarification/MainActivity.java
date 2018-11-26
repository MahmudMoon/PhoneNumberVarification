package com.example.moon.phonenumbervarification;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button btnGetCode;
    Button btnVarify;
    EditText etPhoneNumber,etCode;
    String code_sent;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_views();
        init_variables();
        init_functions();
        init_listeners();
    }

    private void init_views() {
        btnGetCode = (Button)findViewById(R.id.btn_getcode);
        btnVarify = (Button)findViewById(R.id.btn_varify);
        etCode = (EditText)findViewById(R.id.et_enterCode);
        etPhoneNumber = (EditText)findViewById(R.id.et_phone);
    }

    private void init_variables() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void init_functions() {

    }

    private void init_listeners() {
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = etPhoneNumber.getText().toString();
                if(!TextUtils.isEmpty(phone))
                    getcode(phone);
                else{
                    Toast.makeText(getApplicationContext(),"Enter a Valid Phone Number",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVarify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etCode.getText().toString();
                if(!TextUtils.isEmpty(code)){
                    varifyCode(code);
                }else{
                    Toast.makeText(getApplicationContext(),"Invalid Code Entered",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void varifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code_sent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void getcode(String phone) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            code_sent = s;
        }
    };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's informatio

                            FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(MainActivity.this,Home.class);
                            startActivity(intent);



                            // ...
                        } else {

                        }
                    }
                });
    }
}

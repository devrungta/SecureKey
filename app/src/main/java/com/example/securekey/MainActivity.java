package com.example.securekey;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.PhoneAuthOptions;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "PhoneAuthActivity";
    private FirebaseAuth mAuth;
    private String mVerificationID;
    boolean verify_using_otp = false;
    private Button verify_using_otp_btn;
    private EditText username;
    private EditText verify_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();

        Button login_btn = findViewById(R.id.login_button);
        username = findViewById(R.id.Username);
        EditText password = findViewById(R.id.Password);
        verify_using_otp_btn = findViewById(R.id.Verify_Using_OTP_btn);
        verify_otp = findViewById(R.id.VerifyOTP);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Set the login without OTP method and register text button method. Use otp methods for registration from below.

            }
        });
        verify_using_otp_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(verify_using_otp){
                    verifyOTP();

                }else {
                    verify_using_otp = true;
                    verify_otp.setVisibility(View.VISIBLE);
                    username.setHint("Registered Phone Number Please");
                    sendOTP();
                    verify_using_otp_btn.setText("R.strings.Verify_OTP_Button_hint");
                }

            }

            private void verifyOTP() {
                String otp = verify_otp.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, otp);
                signInWithPhoneAuthCredential(credential);
            }

            private void sendOTP() {
                String phoneNumber = username.getText().toString().trim();
                if (!phoneNumber.isEmpty()) {
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(MainActivity.this)                 // Activity (for callback binding)
                                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            // Auto-retrieval of OTP completed by the system
                                            signInWithPhoneAuthCredential(phoneAuthCredential);
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            Log.w(TAG, "onVerificationFailed", e);
                                            Toast.makeText(MainActivity.this, "Verification failed:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String verificationId,
                                                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                            // Save the verification ID somewhere
                                            mVerificationID = verificationId;
                                        }
                                    })          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                }
            }


        });



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithCredential:success");
                    Toast.makeText(MainActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                    //TODO: add the code to move to user specific interface;
                }else{
                    Log.w(TAG, "signInWithCredential:success");
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(MainActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}




package com.example.securekey;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.securekey.database.SecureKeydb;
import com.google.firebase.auth.PhoneAuthOptions;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
    private String phoneNumber;
    private FirebaseAuth mAuth;
    private String mVerificationID;
    boolean verify_using_otp = false;
    private Button verify_using_otp_btn, login_btn, new_Register;
    private EditText username, new_user_E_mail, password,verify_otp,Confirm_password, Phone_number;
    private RadioButton Male, Female;
    private RadioGroup gender;
    boolean in_new_Register = false;
    private SecureKeydb securekeydb = new SecureKeydb(this);
    private MyViewModel viewModel;

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
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        gender = findViewById(R.id.gender);
        Male = findViewById(R.id.Male);
        Female = findViewById(R.id.Female);
        new_user_E_mail = findViewById(R.id.new_user_E_mail);
        new_Register = findViewById(R.id.new_Register);
        login_btn = findViewById(R.id.login_button);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        verify_using_otp_btn = findViewById(R.id.Verify_Using_OTP_btn);
        verify_otp = findViewById(R.id.VerifyOTP);
        Confirm_password = findViewById(R.id.Confirm_password);
        Phone_number = findViewById(R.id.Phone_number);
        // To make sure fields don't lose their values when modes are changed on phone.
        viewModel.getEditTextValue().observe(this, new Observer<String>() {


            @Override
            public void onChanged(String newValue) {
                username.setText(newValue);


            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setEditTextValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        viewModel.getEditTextValue().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                password.setText(s);
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setEditTextValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewModel.getEditTextValue().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s2) {
                new_user_E_mail.setText(s2);
            }
        });
        new_user_E_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s2, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s2, int start, int before, int count) {
                 viewModel.setEditTextValue(s2.toString());
            }

            @Override
            public void afterTextChanged(Editable s2) {

            }
        });
        viewModel.getEditTextValue().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s3) {
                Phone_number.setText(s3);
            }
        });
        Phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s3, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s3, int start, int before, int count) {
                viewModel.setEditTextValue(s3.toString());
            }

            @Override
            public void afterTextChanged(Editable s3) {

            }
        });
        viewModel.getEditTextValue().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s4) {
                verify_otp.setText(s4);
            }
        });
        verify_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s4, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s4, int start, int before, int count) {
                viewModel.setEditTextValue(s4.toString());
            }

            @Override
            public void afterTextChanged(Editable s4) {

            }
        });
        viewModel.getEditTextValue().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s5) {
                Confirm_password.setText(s5);
            }
        });
        Confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s5, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s5, int start, int before, int count) {
                 viewModel.setEditTextValue(s5.toString());
            }

            @Override
            public void afterTextChanged(Editable s5) {

            }
        });


        // All the buttons and their functions...
        new_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!in_new_Register) {
                    login_btn.setVisibility(View.GONE);
                    verify_using_otp_btn.setVisibility(View.GONE);
                    new_user_E_mail.setVisibility(View.VISIBLE);
                    gender.setVisibility(View.VISIBLE);
                    username.setHint(R.string.new_username);
                    Confirm_password.setVisibility(View.VISIBLE);
                    Phone_number.setVisibility(View.VISIBLE);

                    in_new_Register = true;
                } else {

                    if(password.getText().toString().trim().equals(Confirm_password.getText().toString().trim())) {
                        if (Male.isActivated()){

                            long newUserId = securekeydb.addUser(username.getText().toString(), Phone_number.getText().toString(), new_user_E_mail.getText().toString(), password.getText().toString(), String.valueOf(R.string.Male));
                            Log.d("MainActivity", "New user added with ID: " + newUserId);

                        }else{
                            long newUserId = securekeydb.addUser(username.getText().toString(), Phone_number.getText().toString(), new_user_E_mail.getText().toString(), password.getText().toString(), String.valueOf(R.string.Female));
                            Log.d("MainActivity", "New user added with ID: " + newUserId);


                        }

                        Intent intent;

                        intent = new Intent(MainActivity.this, User_specific_Activity.class);
                        intent.putExtra("username", username.getText().toString());
                        intent.putExtra("userid", securekeydb.getUserByName(username.getText().toString()).getUserId());
                        startActivity(intent);



                    }else{
                        Toast.makeText(MainActivity.this, "Confirm Password And Password should match.", Toast.LENGTH_SHORT).show();
                        Confirm_password.setText("");

                    }
                }


            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify_using_otp) {
                    sendOTP();
                } else {
                    String enteredUsername = username.getText().toString().trim();
                    String enteredPassword = password.getText().toString().trim();

                    if (!TextUtils.isEmpty(enteredUsername) && !TextUtils.isEmpty(enteredPassword)) {
                        User user = securekeydb.getUserByName(enteredUsername);
                        if (securekeydb.isUsernameExists(enteredUsername)) {
                            if (enteredPassword.equals(user.getPassword())) {
                                // Passwords match, login successful
                                Intent intent = new Intent(MainActivity.this, User_specific_Activity.class);
                                intent.putExtra("username", enteredUsername);
                                intent.putExtra("userid", user.getUserId());
                                startActivity(intent);
                            } else {
                                // Incorrect password
                                Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // User not found
                            Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Username or password field is empty
                        Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        verify_using_otp_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (verify_using_otp) {
                    verifyOTP();
                } else {
                    verify_using_otp = true;
                    verify_otp.setVisibility(View.VISIBLE);
                    password.setVisibility(View.GONE);
                    username.setHint("Registered Phone Number Please");
                    login_btn.setText(R.string.Send_OTP);
                    new_Register.setVisibility(View.GONE);
                    verify_using_otp_btn.setText(R.string.Verify_OTP_Button_hint);
                }

            }
        });


    }
    // All the function  required to send, receive and authenticate the OTP for login.

   
    private void verifyOTP() {
        String otp = verify_otp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void sendOTP() {
         phoneNumber = username.getText().toString().trim();
        if (!phoneNumber.isEmpty()) {
            if(securekeydb.isPhoneNumberExists(phoneNumber)) {

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
            }else{
                Toast.makeText(this, R.string.unregistered_number , Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "signInWithCredential:success");
                    Toast.makeText(MainActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();


                    Intent intent;
                    intent = new Intent(MainActivity.this, User_specific_Activity.class);
                    intent.putExtra("username",securekeydb.getUserByNumber(phoneNumber).getUsername());
                    intent.putExtra("userid", securekeydb.getUserByNumber(phoneNumber).getUserId());
                    startActivity(intent);


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




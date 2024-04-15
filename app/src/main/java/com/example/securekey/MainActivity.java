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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.securekey.database.SecureKeydb;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;



public class MainActivity extends AppCompatActivity implements SecureKeydb.UserAddListener{
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
    private String newPassword;
    private String newConfirmPassword;
    private String newEmail;
    private String newPhoneNumber;
    private String newOtpVerification;
    private String newUsername;

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
        // Observe the EditText value asynchronously
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPassword = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newConfirmPassword = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        new_user_E_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newEmail = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Add similar text change listeners for other EditText fields...

        // Observe the EditText value asynchronously
        viewModel.getEditTextValue().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String newValue) {
                // Update the UI on the main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update the password EditText field
                        password.setText(newPassword != null ? newPassword : newValue);
                        Confirm_password.setText(newConfirmPassword != null ? newConfirmPassword : newValue);
                        new_user_E_mail.setText(newEmail != null ? newEmail : newValue);
                        // Update other EditText fields similarly...
                    }
                });
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
                        String gender = Male.isChecked() ? getString(R.string.Male) : getString(R.string.Female);
                        securekeydb.addUserAsync(username.getText().toString(),
                                Phone_number.getText().toString(),
                                new_user_E_mail.getText().toString(),
                                password.getText().toString(),
                                gender,
                                (SecureKeydb.UserAddListener) MainActivity.this); // Pass MainActivity as the listener
                        Intent intent = new Intent(MainActivity.this, User_specific_Activity.class);
                        securekeydb.getUserByUsernameAsync(username.getText().toString(), new SecureKeydb.GetUserByUsernameListener() {
                            @Override
                            public void onUserRetrieved(User user) {

                            }

                            @Override
                            public void onUserNotFound() {

                            }
                        });
                        final Long[] user_id = {0L};
                        securekeydb.getUserByUsernameAsync(username.getText().toString(), new SecureKeydb.GetUserByUsernameListener() {
                            @Override
                            public void onUserRetrieved(User user) {
                                user_id[0] = user.getUserId();
                            }

                            @Override
                            public void onUserNotFound() {

                            }
                        });
                        intent.putExtra("username",username.getText().toString());
                        intent.putExtra("userid", user_id[0]);
                        startActivity(intent);

                    }else{
                        Toast.makeText(MainActivity.this, "Confirm Password And Password should match.", Toast.LENGTH_SHORT).show();
                        Confirm_password.setText("");

                    }
                }


            }
        });



        // In MainActivity class

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify_using_otp) {
                    sendOTP();
                } else {
                    String enteredUsername = username.getText().toString().trim();
                    String enteredPassword = password.getText().toString().trim();

                    if (!TextUtils.isEmpty(enteredUsername) && !TextUtils.isEmpty(enteredPassword)) {
                        securekeydb.getUserByUsernameAsync(enteredUsername, new SecureKeydb.GetUserByUsernameListener() {
                            @Override
                            public void onUserRetrieved(User user) {
                                if (user != null) {
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
                            }

                            @Override
                            public void onUserNotFound() {
                                // User not found
                                Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                    String phoneNumber = username.getText().toString().trim();
                    if (!phoneNumber.isEmpty()) {
                        // Check if the phone number exists in the database
                        securekeydb.isPhoneNumberExistsAsync(phoneNumber, new SecureKeydb.PhoneNumberExistsListener() {
                            @Override
                            public void onPhoneNumberExistsCheckCompleted(boolean exists) {
                                if (exists) {
                                    // If the phone number exists, proceed with sending OTP

                                } else {
                                    // If the phone number doesn't exist, show a message
                                    Toast.makeText(MainActivity.this, R.string.unregistered_number , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }





    private void sendOTP() {
        String phoneNumber = username.getText().toString().trim();
        if (!phoneNumber.isEmpty()) {
            securekeydb.isPhoneNumberExistsAsync(phoneNumber, exists -> {
                if (exists) {
                    // Phone number exists, proceed with OTP verification
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

                    // Start phone number verification
                    PhoneAuthProvider.verifyPhoneNumber(options);
                } else {
                    Toast.makeText(MainActivity.this, R.string.unregistered_number, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyOTP() {
        String otp = verify_otp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // If authentication is successful, retrieve user details using phone number
                    String phoneNumber = username.getText().toString().trim();
                    securekeydb.getUserByNumberAsync(phoneNumber, new SecureKeydb.GetUserByNumberListener() {
                        @Override
                        public void onUserRetrieved(User user) {
                            if (user != null) {
                                // If user exists, proceed with login
                                Intent intent = new Intent(MainActivity.this, User_specific_Activity.class);
                                intent.putExtra("username", user.getUsername());
                                intent.putExtra("userid", user.getUserId());
                                startActivity(intent);
                            } else {
                                // If user doesn't exist, show an error message
                                Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Handle authentication failure
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

    @Override
    public void onUserAdded(long newRowId) {

    }

    @Override
    public void onUserAddFailed() {

    }
}




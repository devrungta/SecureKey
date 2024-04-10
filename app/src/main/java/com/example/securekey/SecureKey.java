package com.example.securekey;
import com.google.firebase.FirebaseApp;

import android.app.Application;

public class SecureKey extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}

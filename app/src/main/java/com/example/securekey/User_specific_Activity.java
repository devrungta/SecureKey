package com.example.securekey;

import static com.example.securekey.R.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;




import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

public class User_specific_Activity extends AppCompatActivity implements AppListAdapter.AppClickListener {
    TextView Username_show, no_app_warning;
    FloatingActionButton add_passapp;
    AlertDialog alertDialog;
    String username = null;
    long user_id = 0;
    AppListAdapter adapter;
    List<App> apps;
    MyViewModel viewModel;
    SearchView searchView;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_specific);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.user_specific_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        alertDialog = new AlertDialog.Builder(this).create();

        if (!(intent.getStringExtra("username") == null)) {
            username = intent.getStringExtra("username");
            user_id = intent.getLongExtra("userid", -1);
        } else {
            Intent intent1 = new Intent(User_specific_Activity.this, MainActivity.class);
            startActivity(intent1);
        }
        Username_show = findViewById(R.id.Username_show);
        Username_show.setText(username);
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        add_passapp = findViewById(id.add_passapp);
        no_app_warning = findViewById(id.no_app_warning);
        apps = new ArrayList<>();
        adapter = new AppListAdapter(this, apps, this);
        ListView listView = findViewById(R.id.list_view);
        if (apps.isEmpty()) {
            listView.setVisibility(View.GONE);
            no_app_warning.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            no_app_warning.setVisibility(View.GONE);
            listView.setAdapter(adapter);
        }
        add_passapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_app_dialogue();
            }
        });
        searchView = findViewById(R.id.Search_Box);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterApps(newText);
                return true;
            }
        });

        add_passapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_app_dialogue();
            }
        });
    }

    // Method to filter apps based on search query
    private void filterApps(String query) {
        ArrayList<App> filteredApps = new ArrayList<>();
        for (App app : apps) {
            if (app.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredApps.add(app);
            }
        }
        adapter.setData(filteredApps);
    }





    private void add_app_dialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_app_dialogue, null);
        builder.setView(dialogView);
        EditText appName = dialogView.findViewById(R.id.app_name);
        viewModel = new  ViewModelProvider(this).get(MyViewModel.class);
        viewModel.getEditTextValue().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                appName.setText(s);
            }
        });
        appName.addTextChangedListener(new TextWatcher() {
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
        TextView generatedPasswordTextView = dialogView.findViewById(R.id.generated_password);
        Button regenerateButton = dialogView.findViewById(R.id.regenerate);
        Button acceptButton = dialogView.findViewById(R.id.accept);
        String generatedPassword = generatepassword();
        generatedPasswordTextView.setText(generatedPassword);
        regenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newGeneratedPassword = generatepassword();
                generatedPasswordTextView.setText(newGeneratedPassword);
            }
        });
        acceptButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                add_app();

            }

            private void add_app() {
                String appNameText = appName.getText().toString().trim();
                String generatedPassword = generatedPasswordTextView.getText().toString().trim();

                Log.d("AddAppDialogue", "App Name: " + appNameText);
                Log.d("AddAppDialogue", "Generated Password: " + generatedPassword);
                Log.d("AddAppDialogue", "User ID: " + user_id);

                if (!TextUtils.isEmpty(appNameText)) {
                    App newApp = new App(appNameText, generatedPassword, user_id);
                    apps.add(newApp);
                    adapter.notifyDataSetChanged(); // Notify the adapter of the data change
                    alertDialog.dismiss(); // Dismiss the dialog
                    Log.d("AddAppDialogue", "New App Added: " + newApp.getName());
                } else {
                    Toast.makeText(User_specific_Activity.this, "Enter an app name please.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private String generatepassword() {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        int minLength = 8;
        int maxLength = 13;
        int length = (int) (Math.random() * (maxLength - minLength + 1)) + minLength;
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * allowedChars.length());

            password.append(allowedChars.charAt(index));
        }

        return password.toString();
    }

    public void onAppClick(int position) {
        App clickedApp = apps.get(position);

        showPassword(clickedApp.getPassword());
    }

    private void showPassword(String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password");
        builder.setMessage(password);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

}

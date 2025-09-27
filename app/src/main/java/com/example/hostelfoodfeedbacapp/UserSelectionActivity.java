package com.example.hostelfoodfeedbacapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class UserSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        Button studentBtn = findViewById(R.id.studentBtn);
        Button adminBtn = findViewById(R.id.adminBtn);

        studentBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        adminBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
        });
    }
}

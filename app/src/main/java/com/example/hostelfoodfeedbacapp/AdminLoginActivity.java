package com.example.hostelfoodfeedbacapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLoginActivity extends AppCompatActivity {

    EditText adminEmail, adminPassword;
    Button adminLoginBtn;

    String ADMIN_EMAIL = "warden@hostel.com";
    String ADMIN_PASS = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminEmail = findViewById(R.id.adminEmail);
        adminPassword = findViewById(R.id.adminPassword);
        adminLoginBtn = findViewById(R.id.adminLoginBtn);

        adminLoginBtn.setOnClickListener(v -> {
            String email = adminEmail.getText().toString().trim();
            String password = adminPassword.getText().toString().trim();

            if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASS)) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
            } else {
                Toast.makeText(this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

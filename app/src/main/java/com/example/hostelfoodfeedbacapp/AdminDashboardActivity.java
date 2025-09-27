package com.example.hostelfoodfeedbacapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    RecyclerView feedbackRecyclerView;
    DatabaseReference databaseReference;
    ArrayList<Feedback> feedbackList;
    FeedbackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        feedbackRecyclerView = findViewById(R.id.feedbackRecyclerView);
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedbackList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("feedback");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedbackList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Feedback fb = dataSnapshot.getValue(Feedback.class);
                    if (fb != null) {
                        feedbackList.add(fb);
                    }
                }

                adapter = new FeedbackAdapter(feedbackList);
                feedbackRecyclerView.setAdapter(adapter);
                Log.d("Feedback", "Loaded feedbacks: " + feedbackList.size());
                Toast.makeText(AdminDashboardActivity.this, "Feedbacks Loaded: " + feedbackList.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboardActivity.this, "Failed to load feedbacks: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }
}

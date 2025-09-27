package com.example.hostelfoodfeedbacapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.database.*;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    Spinner daySpinner, mealSpinner;
    RecyclerView menuRecyclerView;
    Button submitButton;
    TextView menuHeader;

    DatabaseReference dbRef;

    String selectedDay = "";
    String selectedMeal = "";

    List<FoodItem> menuItemList = new ArrayList<>();
    MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        daySpinner = findViewById(R.id.daySpinner);
        mealSpinner = findViewById(R.id.mealSpinner);
        menuRecyclerView = findViewById(R.id.menuRecyclerView);
        submitButton = findViewById(R.id.submitButton);
        menuHeader = findViewById(R.id.menuHeader);

        dbRef = FirebaseDatabase.getInstance().getReference("menu");

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] meals = {"Breakfast", "Lunch", "Dinner"};

        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        ArrayAdapter<String> mealAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, meals);
        mealAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealSpinner.setAdapter(mealAdapter);

        adapter = new MenuAdapter(menuItemList);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuRecyclerView.setAdapter(adapter);

        selectedDay = days[0];
        selectedMeal = meals[0];

        fetchMenu();

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay = days[position];
                fetchMenu();
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mealSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMeal = meals[position];
                fetchMenu();
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        submitButton.setOnClickListener(v -> saveRatings());
    }

    private void fetchMenu() {
        dbRef.child(selectedDay).child(selectedMeal).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItemList.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        FoodItem item = itemSnapshot.getValue(FoodItem.class);
                        if (item != null) {
                            menuItemList.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    menuHeader.setText(selectedDay + " " + selectedMeal + " Menu Items:");
                    submitButton.setEnabled(true);
                } else {
                    menuHeader.setText("Menu not available for " + selectedDay + " " + selectedMeal);
                    adapter.notifyDataSetChanged();
                    submitButton.setEnabled(false);
                }
            }

            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load menu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                submitButton.setEnabled(false);
            }
        });
    }

    private void saveRatings() {
        boolean allRated = true;
        for (FoodItem item : menuItemList) {
            if (item.getRating() == 0f) {
                allRated = false;
                break;
            }
        }

        if (!allRated) {
            Toast.makeText(this, "Please rate all menu items before submitting.", Toast.LENGTH_SHORT).show();
            return;
        }

        String feedbackId = FirebaseDatabase.getInstance().getReference("feedback").push().getKey();
        if (feedbackId == null) {
            Toast.makeText(this, "Error generating feedback ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference("feedback").child(feedbackId);

        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("day", selectedDay);
        feedbackData.put("meal", selectedMeal);

        Map<String, Float> ratingsMap = new HashMap<>();
        for (FoodItem item : menuItemList) {
            ratingsMap.put(item.getName(), item.getRating());
        }
        feedbackData.put("ratings", ratingsMap);

        feedbackRef.setValue(feedbackData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
                    for (FoodItem item : menuItemList) {
                        item.setRating(0f);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to submit: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Adapter for RecyclerView
    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

        private final List<FoodItem> menuItems;

        public MenuAdapter(List<FoodItem> menuItems) {
            this.menuItems = menuItems;
        }

        @NonNull
        @Override
        public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.menu_item_rating, parent, false);
            return new MenuViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
            FoodItem item = menuItems.get(position);
            holder.foodNameTextView.setText(item.getName());
            holder.foodRatingBar.setRating(item.getRating());

            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.foodImageView);

            holder.foodRatingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                if (fromUser) {
                    item.setRating(rating);
                }
            });
        }

        @Override
        public int getItemCount() {
            return menuItems.size();
        }

        class MenuViewHolder extends RecyclerView.ViewHolder {
            TextView foodNameTextView;
            RatingBar foodRatingBar;
            ImageView foodImageView;

            public MenuViewHolder(@NonNull View itemView) {
                super(itemView);
                foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
                foodRatingBar = itemView.findViewById(R.id.foodRatingBar);
                foodImageView = itemView.findViewById(R.id.foodImageView);
            }
        }
    }
}

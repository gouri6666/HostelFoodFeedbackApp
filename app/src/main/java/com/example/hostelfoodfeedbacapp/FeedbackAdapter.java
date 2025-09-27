package com.example.hostelfoodfeedbacapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    ArrayList<Feedback> feedbackList;

    public FeedbackAdapter(ArrayList<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback fb = feedbackList.get(position);
        holder.day.setText("Day: " + fb.getDay());
        holder.meal.setText("Meal: " + fb.getMeal());

        StringBuilder ratingsStr = new StringBuilder();
        Map<String, Float> ratings = fb.getRatings();
        if (ratings != null) {
            for (Map.Entry<String, Float> entry : ratings.entrySet()) {
                ratingsStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("  ");
            }
        }
        holder.ratings.setText(ratingsStr.toString());
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView day, meal, ratings;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.dayText);
            meal = itemView.findViewById(R.id.mealText);
            ratings = itemView.findViewById(R.id.ratingsText);
        }
    }
}

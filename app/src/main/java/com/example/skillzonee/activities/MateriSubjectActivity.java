package com.example.skillzonee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.skillzonee.MateriListAdapter;
import com.example.skillzonee.QuizListAdapter;
import com.example.skillzonee.model.MateriModel;
import com.example.skillzonee.R;
import com.example.skillzonee.databinding.ActivitySubjectBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MateriSubjectActivity extends AppCompatActivity {
    private ActivitySubjectBinding binding;
    private List<MateriModel> materiModelList;
    private static final HashMap<String, Integer> SUBJECT_IDS = new HashMap<>();

    static {
        // Mapping subject names to their IDs
        SUBJECT_IDS.put("Matematika", 1);
        SUBJECT_IDS.put("Bahasa Indonesia", 2);
        SUBJECT_IDS.put("Bahasa Inggris", 3);
        SUBJECT_IDS.put("Fisika", 4);
        SUBJECT_IDS.put("Kimia", 5);
        SUBJECT_IDS.put("Biologi", 6);
        SUBJECT_IDS.put("Sejarah", 7);
        SUBJECT_IDS.put("Geografi", 8);
        SUBJECT_IDS.put("Other WKWKWK", 9);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize and set the layout for the binding variable
        binding = ActivitySubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Get intent data
        Intent intent = this.getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            int image = intent.getIntExtra("image", R.drawable.image_logo);
            binding.ImageSlide.setImageResource(image);
            binding.subjectName.setText(name);
        }

        // Initialize quizModelList
        materiModelList = new ArrayList<>();

        // Fetch data from Firebase
        getDataFromFirebase();
        onResume();
    }

    // Set up the RecyclerView
    private void setupRecyclerView() {
        // Hide ProgressBar once data is fetched
        binding.progressBar.setVisibility(View.GONE);

        // Initialize and set the adapter for RecyclerView
        MateriListAdapter adapter = new MateriListAdapter(materiModelList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    // Method to fetch data from Firebase
    private void getDataFromFirebase() {
        // Show ProgressBar while data is being fetched
        binding.progressBar.setVisibility(View.VISIBLE);

        // Reference the root node of the Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Listen for data retrieval success
        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                // Iterate through each child node in dataSnapshot
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Parse dataSnapshot into QuizModel object
                    String subjectName = binding.subjectName.getText().toString();
                    int subjectId = getSubjectId(subjectName);
                    String strSubjectId = String.valueOf(subjectId);
                    MateriModel materiModel = snapshot.getValue(MateriModel.class);
                    if (materiModel != null && materiModel.getId().equals(strSubjectId)) {
                        // Add quizModel to the list
                        materiModelList.add(materiModel);
                    }
                }
            }
            // Set up RecyclerView after data is fetched
            setupRecyclerView();
        }).addOnFailureListener(e -> {
            // Handle any errors during data retrieval
        });
    }

    // Method to get the subject ID from the subject name
    public static int getSubjectId(String subjectName) {
        return SUBJECT_IDS.getOrDefault(subjectName, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

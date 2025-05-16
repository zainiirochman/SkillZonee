package com.example.skillzonee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.skillzonee.QuizListAdapter;
import com.example.skillzonee.model.QuizModel;
import com.example.skillzonee.R;
import com.example.skillzonee.databinding.ActivitySubjectBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {
    private ActivitySubjectBinding binding;
    private List<QuizModel> quizModelList;
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
        SUBJECT_IDS.put("PPKN", 9);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent intent = this.getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            int image = intent.getIntExtra("image", R.drawable.image_logo);
            binding.ImageSlide.setImageResource(image);
            binding.subjectName.setText(name);
        }

        quizModelList = new ArrayList<>();

        getDataFromFirebase();
        onResume();
    }

    private void setupRecyclerView() {
        binding.progressBar.setVisibility(View.GONE);

        QuizListAdapter adapter = new QuizListAdapter(quizModelList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void getDataFromFirebase() {
        binding.progressBar.setVisibility(View.VISIBLE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String subjectName = binding.subjectName.getText().toString();
                    int subjectId = getSubjectId(subjectName);
                    String strSubjectId = String.valueOf(subjectId);
                    QuizModel quizModel = snapshot.getValue(QuizModel.class);
                    if (quizModel != null && quizModel.getId().equals(strSubjectId)) {
                        quizModelList.add(quizModel);
                    }
                }
            }
            setupRecyclerView();
        }).addOnFailureListener(e -> {
        });
    }

    public static int getSubjectId(String subjectName) {
        return SUBJECT_IDS.getOrDefault(subjectName, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

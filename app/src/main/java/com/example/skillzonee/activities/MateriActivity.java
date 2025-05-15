package com.example.skillzonee.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.skillzonee.R;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MateriActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Arrays to hold subject names, CardView IDs, and image resource IDs
    private final String[] subjectNames = {
            "Matematika",
            "Bahasa Indonesia",
            "Bahasa Inggris",
            "Fisika",
            "Kimia",
            "Biologi",
            "Sejarah",
            "Geografi",
            "Other WKWKWK"
    };
    private final int[] cardViewIds = {
            R.id.mathsCard,
            R.id.literatureCard,
            R.id.englishCard,
            R.id.physicsCard,
            R.id.chemistryCard,
            R.id.biologyCard,
            R.id.historyCard,
            R.id.geographyCard,
            R.id.civicEducationCard
    };
    private final int[] imageResIds = {
            R.drawable.image_maths,
            R.drawable.image_literature,
            R.drawable.image_english,
            R.drawable.image_physics,
            R.drawable.image_chemistry,
            R.drawable.image_biology,
            R.drawable.image_history,
            R.drawable.image_geography,
            R.drawable.image_civiceducation
    };

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        loadQuiz();

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SkillZone");
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        View headerView = navigationView.getHeaderView(0);

        TextView textViewEmail = headerView.findViewById(R.id.textViewEmail);
        TextView textViewRole = headerView.findViewById(R.id.textViewRole);
        if (currentUser != null) {
            String uid = currentUser.getUid();

            userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String email = snapshot.child("email").getValue(String.class);
                        String role = snapshot.child("role").getValue(String.class);

                        textViewEmail.setText(email != null ? email : "Email tidak ditemukan");
                        textViewRole.setText(role != null ? role : "Role tidak ditemukan");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FIREBASE", "Gagal mengambil data user: " + error.getMessage());
                }
            });
        }
    }

    // Method to load the image slider with images from Firebase Storage

    // Method to load the quiz cards with their respective images and names
    private void loadQuiz() {
        for (int i = 0; i < cardViewIds.length; i++) {
            CardView cardView = findViewById(cardViewIds[i]);
            final int index = i; // Save index for OnClickListener
            cardView.setOnClickListener(v -> navigateToSubject(imageResIds[index], subjectNames[index]));
        }
    }

    // Method to navigate to the SubjectActivity with the selected subject's image and name
    private void navigateToSubject(int imageResId, String subjectName) {
        Intent subjectActivity = new Intent(MateriActivity.this, SubjectActivity.class);
        subjectActivity.putExtra("image", imageResId);
        subjectActivity.putExtra("name", subjectName);
        startActivity(subjectActivity);
    }

    // Handle navigation item selections
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_material) {
            Intent intent = new Intent(MateriActivity.this, MateriActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }

        if (id == R.id.nav_quiz) {
            Intent intent = new Intent(MateriActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.nav_logout) {
            mAuth.signOut(); // Logout dari Firebase
            Intent intent = new Intent(MateriActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Hapus backstack
            startActivity(intent);
            finish();
            return true;
        }

        return true;
    }
}

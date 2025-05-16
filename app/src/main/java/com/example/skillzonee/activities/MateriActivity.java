package com.example.skillzonee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillzonee.MateriAdapter;
import com.example.skillzonee.R;
import com.example.skillzonee.model.MateriModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MateriActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private List<MateriModel> materiList;
    private MateriAdapter adapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerViewMateri);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        materiList = new ArrayList<>();
        adapter = new MateriAdapter(materiList);
        recyclerView.setAdapter(adapter);

        loadMateriFromFirebase();

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

    private void loadMateriFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Materi");
        ref.get().addOnSuccessListener(dataSnapshot -> {
            materiList.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                MateriModel model = snapshot.getValue(MateriModel.class);
                if (model != null) {
                    materiList.add(model);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

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

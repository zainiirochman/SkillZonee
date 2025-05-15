package com.example.skillzonee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skillzonee.R;
import com.example.skillzonee.model.LoginModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private RadioButton btnRoleGuru, btnRoleSiswa;
    private ProgressBar progressBar;
    private Button btnLogin, btnSignUp;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi komponen UI
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnRoleGuru = findViewById(R.id.btnRoleGuru);
        btnRoleSiswa = findViewById(R.id.btnRoleSiswa);
        progressBar = findViewById(R.id.progressBar);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Tombol Login
        btnLogin.setOnClickListener(v -> loginUser());

        // Tombol Sign Up
        btnSignUp.setOnClickListener(v -> signUpUser());
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Isi semua data!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();

                // Ambil role dari database
                databaseReference.child(user.getUid()).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String role = snapshot.getValue(String.class);
                        if (role.equals("Guru")) {
                            startActivity(new Intent(LoginActivity.this, GuruActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, MateriActivity.class));
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Gagal membaca role.", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(LoginActivity.this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUpUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String role = btnRoleGuru.isChecked() ? "Guru" : btnRoleSiswa.isChecked() ? "Siswa" : "";

        if (email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "Isi semua data termasuk role!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();

                // Simpan email & role ke Realtime Database
                if (user != null) {
                    databaseReference.child(user.getUid()).setValue(new User(email, role));
                    Toast.makeText(LoginActivity.this, "Pendaftaran berhasil, silakan login.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Pendaftaran gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Model class
    public static class User {
        public String email, role;

        public User() {}

        public User(String email, String role) {
            this.email = email;
            this.role = role;
        }
    }
}
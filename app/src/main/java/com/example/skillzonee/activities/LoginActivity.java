package com.example.skillzonee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skillzonee.R;
import com.example.skillzonee.model.LoginModel;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Auto redirect jika sudah login
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressBar);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(v -> loginUser());
        btnSignUp.setOnClickListener(v -> signUpUser());
    }

    private boolean validateForm(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email harus diisi");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Format email tidak valid");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password harus diisi");
            return false;
        }
        if (password.length() < 6) {
            passwordInput.setError("Password minimal 6 karakter");
            return false;
        }
        return true;
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!validateForm(email, password)) return;

        progressBar.setVisibility(View.VISIBLE);

        LoginModel loginModel = new LoginModel(email, password);

        mAuth.signInWithEmailAndPassword(loginModel.getEmail(), loginModel.getPassword())
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Login gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void signUpUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!validateForm(email, password)) return;

        progressBar.setVisibility(View.VISIBLE);

        LoginModel loginModel = new LoginModel(email, password);

        mAuth.createUserWithEmailAndPassword(loginModel.getEmail(), loginModel.getPassword())
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Pendaftaran berhasil, masuk sebagai " + loginModel.getEmail(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Pendaftaran gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
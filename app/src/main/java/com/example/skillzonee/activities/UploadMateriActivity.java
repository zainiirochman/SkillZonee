package com.example.skillzonee.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skillzonee.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UploadMateriActivity extends AppCompatActivity {

    private EditText editTitle, editDescription, editYoutubeLink;
    private Button btnUpload;
    private DatabaseReference materiRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_materi);

        editTitle = findViewById(R.id.editTextTitle);
        editDescription = findViewById(R.id.editTextDescription);
        editYoutubeLink = findViewById(R.id.editTextYoutubeUrl);
        btnUpload = findViewById(R.id.buttonUploadMateri);

        materiRef = FirebaseDatabase.getInstance().getReference("Materi");

        btnUpload.setOnClickListener(v -> uploadMateri());
    }

    private void uploadMateri() {
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String youtubeUrl = editYoutubeLink.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(youtubeUrl)) {
            Toast.makeText(this, "Semua kolom wajib diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        materiRef.get().addOnSuccessListener(snapshot -> {
            long count = snapshot.getChildrenCount();
            String newKey = String.valueOf(count + 1);

            HashMap<String, Object> materiData = new HashMap<>();
            materiData.put("title", title);
            materiData.put("description", description);
            materiData.put("youtubeLink", youtubeUrl);

            materiRef.child(newKey).setValue(materiData)
                    .addOnSuccessListener(unused -> Toast.makeText(this, "Materi berhasil diunggah", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Gagal mengunggah: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Gagal membaca database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

}

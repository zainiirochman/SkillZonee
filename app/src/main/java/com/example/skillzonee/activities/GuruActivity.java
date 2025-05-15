package com.example.skillzonee.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skillzonee.R;
import com.example.skillzonee.model.GuruModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.*;

public class GuruActivity extends AppCompatActivity {

    private EditText editTextQuizId, editTextQuizTitle, editTextQuizSubtitle, editTextQuizTime;
    private LinearLayout questionContainer;
    private Button btnAddQuestion, btnSubmitQuiz;

    private List<GuruModel.QuestionModel> questionList = new ArrayList<>();
    private FirebaseDatabase database;
    private DatabaseReference quizRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru); // pastikan layout ini sesuai nama XML kamu

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance();
        quizRef = database.getReference("QuizModel");

        // Inisialisasi view
        editTextQuizId = findViewById(R.id.editTextQuizId);
        editTextQuizTitle = findViewById(R.id.editTextQuizTitle);
        editTextQuizSubtitle = findViewById(R.id.editTextQuizSubtitle);
        editTextQuizTime = findViewById(R.id.editTextQuizTime);
        questionContainer = findViewById(R.id.questionContainer);
        btnAddQuestion = findViewById(R.id.btnAddQuestion);
        btnSubmitQuiz = findViewById(R.id.btnSubmitQuiz);

        btnAddQuestion.setOnClickListener(v -> addQuestionView());
        btnSubmitQuiz.setOnClickListener(v -> submitQuizToFirebase());
    }

    private void addQuestionView() {
        View questionView = getLayoutInflater().inflate(R.layout.item_question, null);

        questionContainer.addView(questionView);
    }

    private void submitQuizToFirebase() {
        String id = editTextQuizId.getText().toString().trim();
        String title = editTextQuizTitle.getText().toString().trim();
        String subtitle = editTextQuizSubtitle.getText().toString().trim();
        String time = editTextQuizTime.getText().toString().trim();

        questionList.clear();

        for (int i = 0; i < questionContainer.getChildCount(); i++) {
            View qView = questionContainer.getChildAt(i);

            EditText etQuestion = qView.findViewById(R.id.editTextQuestion);
            EditText etOption1 = qView.findViewById(R.id.editTextOption1);
            EditText etOption2 = qView.findViewById(R.id.editTextOption2);
            EditText etOption3 = qView.findViewById(R.id.editTextOption3);
            EditText etOption4 = qView.findViewById(R.id.editTextOption4);
            EditText etCorrect = qView.findViewById(R.id.editTextCorrect);

            List<String> options = Arrays.asList(
                    etOption1.getText().toString().trim(),
                    etOption2.getText().toString().trim(),
                    etOption3.getText().toString().trim(),
                    etOption4.getText().toString().trim()
            );

            GuruModel.QuestionModel question = new GuruModel.QuestionModel(
                    etQuestion.getText().toString().trim(),
                    etCorrect.getText().toString().trim(),
                    options
            );

            questionList.add(question);
        }

        GuruModel quiz = new GuruModel(id, title, subtitle, time, questionList);
        quizRef.child(id).setValue(quiz)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Kuis berhasil disimpan", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal menyimpan kuis", Toast.LENGTH_SHORT).show());
    }
}

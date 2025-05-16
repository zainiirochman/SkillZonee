package com.example.skillzonee.activities;

import static java.lang.String.format;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skillzonee.model.QuestionModel;
import com.example.skillzonee.R;
import com.example.skillzonee.databinding.ActivityQuizBinding;
import com.example.skillzonee.databinding.ItemScoreDialogBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private static List<QuestionModel> questionModelList;
    private static String time;
    private ActivityQuizBinding binding;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";
    private int score = 0;

    public static void setQuestionModelList(List<QuestionModel> questionList) {
        questionModelList = questionList;
    }

    public static void setTime(String time) {
        QuizActivity.time = time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btn0.setOnClickListener(this);
        binding.btn1.setOnClickListener(this);
        binding.btn2.setOnClickListener(this);
        binding.btn3.setOnClickListener(this);
        binding.nextBtn.setOnClickListener(this);
        binding.endBtn.setOnClickListener(this);

        loadQuestions();
        startTimer();
    }

    private void startTimer() {
        long totalTimeInMillis = Integer.parseInt(time) * 60 * 1000L;
        new CountDownTimer(totalTimeInMillis, 1000L) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long remainingSeconds = seconds % 60;
                binding.timerIndicatorTextview.setText(format("%02d:%02d", minutes, remainingSeconds));
            }

            @Override
            public void onFinish() {
                finishQuiz();
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    private void loadQuestions() {
        selectedAnswer = "";
        if (currentQuestionIndex == questionModelList.size()) {
            finishQuiz();
            return;
        }
        binding.questionIndicatorTextview.setText("Question " + (currentQuestionIndex + 1) + "/ " + questionModelList.size());
        binding.questionProgressIndicator.setProgress((int) ((currentQuestionIndex / (float) questionModelList.size()) * 100));
        binding.questionTextview.setText(questionModelList.get(currentQuestionIndex).getQuestion());
        List<String> options = questionModelList.get(currentQuestionIndex).getOptions();
        binding.btn0.setText(options.get(0));
        binding.btn1.setText(options.get(1));
        binding.btn2.setText(options.get(2));
        binding.btn3.setText(options.get(3));
    }

    @Override
    public void onClick(View view) {
        binding.btn0.setBackgroundColor(getColor(R.color.gray));
        binding.btn1.setBackgroundColor(getColor(R.color.gray));
        binding.btn2.setBackgroundColor(getColor(R.color.gray));
        binding.btn3.setBackgroundColor(getColor(R.color.gray));

        try {
            if (view.getId() == R.id.next_btn) {
                if (selectedAnswer.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Pilih jawaban sebelum melanjutkan", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectedAnswer.equals(questionModelList.get(currentQuestionIndex).getCorrect())) {
                    score++;
                    Log.i("Nilai", valueOf(score));
                }
                currentQuestionIndex++;
                loadQuestions();
            } else if (view.getId() == R.id.end_btn) {
                onExitButtonClick();
            } else {
                selectedAnswer = ((Button) view).getText().toString();
                view.setBackgroundColor(getColor(R.color.orange));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Application error", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void finishQuiz() {
        try {
            int totalQuestions = questionModelList.size();
            int percentage = (int) (((float) score / totalQuestions) * 100);

            ItemScoreDialogBinding dialogBinding = ItemScoreDialogBinding.inflate(getLayoutInflater());
            dialogBinding.scoreProgressIndicator.setProgress(percentage);
            dialogBinding.scoreProgressText.setText(percentage + " %");
            dialogBinding.scoreTitle.setText("Kamu telah selesai mengerjakan kuis");
            dialogBinding.scoreTitle.setTextColor(Color.BLACK);
            dialogBinding.scoreSubtitle.setText(score + " jawaban benar dari " + totalQuestions + " soal");

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String email = currentUser.getEmail();
                dialogBinding.textEmailScore.setText(email);
            } else {
                dialogBinding.textEmailScore.setText("Email tidak tersedia");
            }

            dialogBinding.finishBtn.setOnClickListener(v -> finish());

            new AlertDialog.Builder(this)
                    .setView(dialogBinding.getRoot())
                    .setCancelable(false)
                    .show();
        } catch (Exception e) {
            Toast.makeText(this, "Application error", Toast.LENGTH_SHORT).show();
        }
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah kamu yakin keluar dari kuis?")
                .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishQuiz();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    public void onExitButtonClick() {
        showExitConfirmationDialog();
    }
}

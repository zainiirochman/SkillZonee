package com.example.skillzonee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillzonee.activities.QuizActivity;
import com.example.skillzonee.model.QuizModel;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.MyViewHolder> {

    private final List<QuizModel> quizModelList;

    public QuizListAdapter(List<QuizModel> quizModelList) {
        this.quizModelList = quizModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_recycler_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(quizModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return quizModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView quizTitleText;
        private final TextView quizSubtitleText;
        private final TextView quizTimeText;

        public MyViewHolder(View itemView) {
            super(itemView);
            quizTitleText = itemView.findViewById(R.id.quiz_title_text);
            quizSubtitleText = itemView.findViewById(R.id.quiz_subtitle_text);
            quizTimeText = itemView.findViewById(R.id.quiz_time_text);
        }

        public void bind(QuizModel model) {
            quizTitleText.setText(model.getTitle());
            quizSubtitleText.setText(model.getSubtitle());
            quizTimeText.setText(model.getTime() + " menit");

            itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, QuizActivity.class);
                QuizActivity.setQuestionModelList(model.getQuestionList());
                QuizActivity.setTime(model.getTime());
                context.startActivity(intent);
            });
        }
    }
}

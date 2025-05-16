package com.example.skillzonee;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skillzonee.model.MateriModel;
import java.util.List;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.ViewHolder> {
    private final List<MateriModel> materiList;

    public MateriAdapter(List<MateriModel> materiList) {
        this.materiList = materiList;
    }

    @NonNull
    @Override
    public MateriAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_materi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriAdapter.ViewHolder holder, int position) {
        MateriModel materi = materiList.get(position);
        holder.title.setText(materi.getTitle());
        holder.desc.setText(materi.getDescription());

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(materi.getYoutubeLink()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return materiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.materi_title);
            desc = itemView.findViewById(R.id.materi_desc);
        }
    }
}

package br.com.unemat.paulo.atividadeavaliativa.ui.grade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.data.model.Grade;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    private List<Grade> gradeList = new ArrayList<>();

    public void submitList(List<Grade> grades) {
        this.gradeList = grades;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder h, int pos) {
        Grade g = gradeList.get(pos);
        h.txtDisciplina.setText(g.getSubject().getName());
        h.txtNota1.setText(String.format("%.1f", g.getGradeValue()));
        h.txtNota2.setText("-");
        h.txtMedia.setText("-");
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView txtDisciplina, txtNota1, txtNota2, txtMedia;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDisciplina = itemView.findViewById(R.id.txtDisciplina);
            txtNota1 = itemView.findViewById(R.id.txtNota1);
            txtNota2 = itemView.findViewById(R.id.txtNota2);
            txtMedia = itemView.findViewById(R.id.txtMedia);
        }
    }
}
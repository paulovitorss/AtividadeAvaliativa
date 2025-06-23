package br.com.unemat.paulo.atividadeavaliativa.ui.grade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.data.model.SubjectGrade;

public class SubjectGradeAdapter extends RecyclerView.Adapter<SubjectGradeAdapter.VH> {

    private List<SubjectGrade> list = new ArrayList<>();

    public void submitList(List<SubjectGrade> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject_grade, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        SubjectGrade sg = list.get(pos);
        h.txtDisciplina.setText(sg.getSubjectName());

        h.llGrades.removeAllViews();

        for (BigDecimal grade : sg.getGrades()) {
            TextView tv = new TextView(h.itemView.getContext());
            tv.setText(String.format("%.1f", grade));
            tv.setTextSize(14);
            tv.setPadding(16, 0, 16, 0);
            h.llGrades.addView(tv);
        }

        List<BigDecimal> grades = sg.getGrades();
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal g : grades) sum = sum.add(g);
        BigDecimal avg = grades.isEmpty()
                ? BigDecimal.ZERO
                : sum.divide(BigDecimal.valueOf(grades.size()), 1, RoundingMode.HALF_UP);

        h.txtMedia.setText(String.format("%.1f", avg));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtDisciplina, txtMedia;
        LinearLayout llGrades;

        public VH(@NonNull View itemView) {
            super(itemView);
            txtDisciplina = itemView.findViewById(R.id.txtDisciplina);
            llGrades = itemView.findViewById(R.id.llGradesContainer);
            txtMedia = itemView.findViewById(R.id.txtMedia);
        }
    }
}
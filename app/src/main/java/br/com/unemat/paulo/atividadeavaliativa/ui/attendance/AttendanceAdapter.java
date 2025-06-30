package br.com.unemat.paulo.atividadeavaliativa.ui.attendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.data.model.AttendanceSummary;

public class AttendanceAdapter extends ListAdapter<AttendanceSummary, AttendanceAdapter.AttendanceViewHolder> {

    public AttendanceAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frequencia, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        AttendanceSummary summary = getItem(position);
        holder.bind(summary);
    }

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtDisciplina;
        private final TextView txtTotalAulas;
        private final TextView txtPresencas;
        private final TextView txtPercentual;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDisciplina = itemView.findViewById(R.id.txtDisciplina);
            txtTotalAulas = itemView.findViewById(R.id.txtTotalAulas);
            txtPresencas = itemView.findViewById(R.id.txtPresencas);
            txtPercentual = itemView.findViewById(R.id.txtPercentual);
        }

        public void bind(AttendanceSummary summary) {
            txtDisciplina.setText(summary.getSubjectName());
            txtTotalAulas.setText(String.valueOf(summary.getTotalClasses()));
            txtPresencas.setText(String.valueOf(summary.getPresentClasses()));
            txtPercentual.setText(summary.getFormattedPercentage() + "%");
        }
    }

    private static final DiffUtil.ItemCallback<AttendanceSummary> DIFF_CALLBACK = new DiffUtil.ItemCallback<AttendanceSummary>() {
        @Override
        public boolean areItemsTheSame(@NonNull AttendanceSummary oldItem, @NonNull AttendanceSummary newItem) {
            return oldItem.getSubjectName().equals(newItem.getSubjectName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull AttendanceSummary oldItem, @NonNull AttendanceSummary newItem) {
            return oldItem.getTotalClasses() == newItem.getTotalClasses() && oldItem.getPresentClasses() == newItem.getPresentClasses();
        }
    };
}
package br.com.unemat.paulo.atividadeavaliativa.ui.attendance;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import br.com.unemat.paulo.atividadeavaliativa.data.model.AttendanceSummary;
import br.com.unemat.paulo.atividadeavaliativa.databinding.ItemAttendanceBinding;

public class AttendanceAdapter extends ListAdapter<AttendanceSummary, AttendanceAdapter.AttendanceViewHolder> {

    public AttendanceAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAttendanceBinding binding = ItemAttendanceBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AttendanceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        private final ItemAttendanceBinding binding;

        public AttendanceViewHolder(ItemAttendanceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AttendanceSummary summary) {
            binding.attendanceRow.txtDisciplina.setText(summary.subjectName());
            binding.attendanceRow.txtTotalAulas.setText(String.valueOf(summary.totalClasses()));
            binding.attendanceRow.txtPresencas.setText(String.valueOf(summary.presentClasses()));
            binding.attendanceRow.txtPercentual.setText(summary.getFormattedPercentage());
        }
    }

    private static final DiffUtil.ItemCallback<AttendanceSummary> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull AttendanceSummary oldItem, @NonNull AttendanceSummary newItem) {
            return oldItem.subjectName().equals(newItem.subjectName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull AttendanceSummary oldItem, @NonNull AttendanceSummary newItem) {
            return oldItem.equals(newItem);
        }
    };
}
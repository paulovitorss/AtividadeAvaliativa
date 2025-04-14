package br.com.unemat.paulo.atividadeavaliativa.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.controller.AlunoController;
import br.com.unemat.paulo.atividadeavaliativa.controller.FrequenciaController;
import br.com.unemat.paulo.atividadeavaliativa.model.Frequencia;

public class FrequenciaAdapter extends RecyclerView.Adapter<FrequenciaAdapter.FrequenciaViewHolder> {

    private List<Frequencia> listaFrequencias;
    private final Context context;
    private final AlunoController alunoController;
    private final FrequenciaController frequenciaController;

    public FrequenciaAdapter(List<Frequencia> listaFrequencias, Context context) {
        this.listaFrequencias = listaFrequencias;
        this.context = context;
        this.alunoController = new AlunoController(context);
        this.frequenciaController = new FrequenciaController(context);
    }

    @NonNull
    @Override
    public FrequenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_frequencia_gerenciar, parent, false);
        return new FrequenciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FrequenciaViewHolder holder, int position) {
        Frequencia frequencia = listaFrequencias.get(position);
        String nomeAluno = alunoController.getNomeAlunoPorId(String.valueOf(frequencia.getAlunoId()));

        holder.txtAluno.setText(nomeAluno);
        holder.txtDisciplina.setText(frequencia.getDisciplina());
        holder.txtTotalAulas.setText(String.valueOf(frequencia.getTotalAulas()));
        holder.txtAulasPresentes.setText(String.valueOf(frequencia.getAulasPresentes()));

        int porcentagem = (int) ((float) frequencia.getAulasPresentes() / frequencia.getTotalAulas() * 100);
        holder.txtPorcentagem.setText(porcentagem + "%");

        // Definir a cor do texto da porcentagem
        int colorRes = (porcentagem < 75) ? android.R.color.holo_red_dark : android.R.color.holo_green_dark;
        holder.txtPorcentagem.setTextColor(context.getResources().getColor(colorRes));

        holder.btnRemover.setOnClickListener(v -> {
            frequenciaController.removerFrequencia(frequencia.getAlunoId(), frequencia.getDisciplina());
            listaFrequencias.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            Toast.makeText(context, "Registro removido", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listaFrequencias.size();
    }

    public void atualizarLista(List<Frequencia> novaLista) {
        this.listaFrequencias = novaLista;
        notifyDataSetChanged();
    }

    static class FrequenciaViewHolder extends RecyclerView.ViewHolder {
        TextView txtAluno, txtDisciplina, txtTotalAulas, txtAulasPresentes, txtPorcentagem;
        Button btnRemover;

        public FrequenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAluno = itemView.findViewById(R.id.txtAluno);
            txtDisciplina = itemView.findViewById(R.id.txtDisciplina);
            txtTotalAulas = itemView.findViewById(R.id.txtTotalAulas);
            txtAulasPresentes = itemView.findViewById(R.id.txtAulasPresentes);
            txtPorcentagem = itemView.findViewById(R.id.txtPorcentagem);
            btnRemover = itemView.findViewById(R.id.btnRemover);
        }
    }
}

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
import br.com.unemat.paulo.atividadeavaliativa.controller.NotaController;
import br.com.unemat.paulo.atividadeavaliativa.model.Nota;

public class GerenciarNotasAdapter extends RecyclerView.Adapter<GerenciarNotasAdapter.NotaViewHolder> {

    private final AlunoController alunoController;
    private final NotaController notaController;
    private final Context context;
    private List<Nota> listaNotas;

    public GerenciarNotasAdapter(List<Nota> listaNotas, Context context) {
        this.listaNotas = listaNotas;
        this.context = context;
        this.notaController = new NotaController(context);
        this.alunoController = new AlunoController(context);
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nota_gerenciar, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Nota nota = listaNotas.get(position);

        holder.txtAluno.setText(nota.getAlunoNome());
        holder.txtDisciplina.setText(nota.getDisciplina());
        holder.txtNota1.setText(String.format("%.1f", nota.getNota1()));
        holder.txtNota2.setText(String.format("%.1f", nota.getNota2()));
        holder.txtMedia.setText(String.format("%.1f", nota.getMedia()));

        holder.btnRemover.setOnClickListener(v -> {
            notaController.removerNota(nota.getAlunoId(), nota.getDisciplina());
            listaNotas.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            Toast.makeText(context, "Nota removida", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }

    public void atualizarLista(List<Nota> novaLista) {
        this.listaNotas = novaLista;
        notifyDataSetChanged();
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView txtAluno, txtDisciplina, txtNota1, txtNota2, txtMedia;
        Button btnRemover;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAluno = itemView.findViewById(R.id.txtAluno);
            txtDisciplina = itemView.findViewById(R.id.txtDisciplina);
            txtNota1 = itemView.findViewById(R.id.txtNota1);
            txtNota2 = itemView.findViewById(R.id.txtNota2);
            txtMedia = itemView.findViewById(R.id.txtMedia);
            btnRemover = itemView.findViewById(R.id.btnRemover);
        }
    }
}
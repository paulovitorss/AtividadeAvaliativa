package br.com.unemat.paulo.atividadeavaliativa.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.controller.AlunoController;
import br.com.unemat.paulo.atividadeavaliativa.model.Aluno;

public class AlunosAdapter extends RecyclerView.Adapter<AlunosAdapter.AlunoViewHolder> {

    private final AlunoController controller;
    private List<Aluno> alunos;

    public AlunosAdapter(List<Aluno> alunos, AlunoController controller) {
        this.alunos = alunos;
        this.controller = controller;
    }

    @Override
    public AlunoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aluno, parent, false);
        return new AlunoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlunoViewHolder holder, int position) {
        Aluno aluno = alunos.get(position);
        holder.txtNome.setText(aluno.getNome());
        holder.txtSerie.setText(aluno.getSerie());
        holder.txtId.setText("Matrícula: " + aluno.getMatricula());

        holder.btnRemover.setOnClickListener(v -> {
            String matricula = aluno.getMatricula();
            controller.removerAluno(matricula);
            alunos.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(v.getContext(), "Aluno removido", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    // Correção: usar o campo correto (alunos)
    public void atualizarLista(List<Aluno> novaLista) {
        this.alunos = novaLista;
        notifyDataSetChanged();
    }

    public static class AlunoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtSerie, txtId;
        Button btnRemover;

        public AlunoViewHolder(View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeAluno);
            txtSerie = itemView.findViewById(R.id.txtSerieAluno);
            txtId = itemView.findViewById(R.id.txtIdAluno);
            btnRemover = itemView.findViewById(R.id.btnRemoverAluno);
        }
    }
}
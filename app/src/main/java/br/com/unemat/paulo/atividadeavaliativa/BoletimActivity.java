package br.com.unemat.paulo.atividadeavaliativa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BoletimActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boletim);

        // Inicializar componentes
        TextView txtTitulo = findViewById(R.id.txtTitulo);
        recyclerViewNotas = findViewById(R.id.recyclerViewNotas);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        // Configurar título
        txtTitulo.setText("Boletim Escolar - 2º Bimestre");

        // Configurar RecyclerView com dados simulados
        setupRecyclerView();

        // Configurar listener para o botão de voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        // Dados simulados para o boletim
        List<NotaItem> notasList = new ArrayList<>();
        notasList.add(new NotaItem("Matemática", 8.5f, 7.5f, 8.0f));
        notasList.add(new NotaItem("Português", 7.0f, 8.0f, 7.5f));
        notasList.add(new NotaItem("Ciências", 9.0f, 8.5f, 8.8f));
        notasList.add(new NotaItem("História", 7.5f, 8.0f, 7.8f));
        notasList.add(new NotaItem("Geografia", 8.0f, 7.0f, 7.5f));
        notasList.add(new NotaItem("Inglês", 9.5f, 9.0f, 9.3f));
        notasList.add(new NotaItem("Educação Física", 10.0f, 10.0f, 10.0f));
        notasList.add(new NotaItem("Artes", 8.5f, 9.0f, 8.8f));

        // Configurar adapter e layout manager
        NotasAdapter adapter = new NotasAdapter(notasList);
        recyclerViewNotas.setAdapter(adapter);
        recyclerViewNotas.setLayoutManager(new LinearLayoutManager(this));
    }

    // Classe modelo para os itens de nota
    public static class NotaItem {
        private String disciplina;
        private float nota1;
        private float nota2;
        private float media;

        public NotaItem(String disciplina, float nota1, float nota2, float media) {
            this.disciplina = disciplina;
            this.nota1 = nota1;
            this.nota2 = nota2;
            this.media = media;
        }

        public String getDisciplina() {
            return disciplina;
        }

        public float getNota1() {
            return nota1;
        }

        public float getNota2() {
            return nota2;
        }

        public float getMedia() {
            return media;
        }
    }

    // Adapter para o RecyclerView de notas
    public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.NotaViewHolder> {
        private List<NotaItem> notasList;

        public NotasAdapter(List<NotaItem> notasList) {
            this.notasList = notasList;
        }

        @Override
        public NotaViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_nota, parent, false);
            return new NotaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NotaViewHolder holder, int position) {
            NotaItem item = notasList.get(position);
            holder.txtDisciplina.setText(item.getDisciplina());
            holder.txtNota1.setText(String.format("%.1f", item.getNota1()));
            holder.txtNota2.setText(String.format("%.1f", item.getNota2()));
            holder.txtMedia.setText(String.format("%.1f", item.getMedia()));
        }

        @Override
        public int getItemCount() {
            return notasList.size();
        }

        public class NotaViewHolder extends RecyclerView.ViewHolder {
            TextView txtDisciplina, txtNota1, txtNota2, txtMedia;

            public NotaViewHolder(View itemView) {
                super(itemView);
                txtDisciplina = itemView.findViewById(R.id.txtDisciplina);
                txtNota1 = itemView.findViewById(R.id.txtNota1);
                txtNota2 = itemView.findViewById(R.id.txtNota2);
                txtMedia = itemView.findViewById(R.id.txtMedia);
            }
        }
    }
}


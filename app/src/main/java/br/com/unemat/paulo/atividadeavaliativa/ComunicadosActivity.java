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

public class ComunicadosActivity extends AppCompatActivity {
    private TextView txtTitulo;
    private RecyclerView recyclerViewComunicados;
    private Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicados);

        // Inicializar componentes
        txtTitulo = findViewById(R.id.txtTitulo);
        recyclerViewComunicados = findViewById(R.id.recyclerViewComunicados);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Configurar título
        txtTitulo.setText("Comunicados Escolares");

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
        // Dados simulados para os comunicados
        List<ComunicadoItem> comunicadosList = new ArrayList<>();
        comunicadosList.add(new ComunicadoItem(
                "Reunião de Pais e Mestres",
                "Prezados pais e responsáveis, informamos que a reunião de pais e mestres será realizada no dia 15/06/2023 às 19h no auditório da escola.",
                "05/06/2023",
                "Direção Escolar"));

        comunicadosList.add(new ComunicadoItem(
                "Feira de Ciências",
                "A Feira de Ciências acontecerá nos dias 20 e 21 de junho. Os alunos devem preparar seus projetos conforme orientação dos professores de Ciências.",
                "01/06/2023",
                "Coordenação Pedagógica"));

        comunicadosList.add(new ComunicadoItem(
                "Recesso Escolar",
                "Informamos que o recesso escolar do meio do ano será de 10/07/2023 a 24/07/2023. As aulas retornarão normalmente no dia 25/07/2023.",
                "25/05/2023",
                "Secretaria Escolar"));

        comunicadosList.add(new ComunicadoItem(
                "Olimpíada de Matemática",
                "As inscrições para a Olimpíada Brasileira de Matemática estão abertas até o dia 10/06/2023. Interessados devem procurar o professor de Matemática.",
                "20/05/2023",
                "Departamento de Matemática"));

        comunicadosList.add(new ComunicadoItem(
                "Campeonato Esportivo",
                "O campeonato esportivo interclasses acontecerá na última semana de junho. As modalidades incluem futsal, vôlei e basquete.",
                "15/05/2023",
                "Departamento de Educação Física"));

        // Configurar adapter e layout manager
        ComunicadosAdapter adapter = new ComunicadosAdapter(comunicadosList);
        recyclerViewComunicados.setAdapter(adapter);
        recyclerViewComunicados.setLayoutManager(new LinearLayoutManager(this));
    }

    // Classe modelo para os itens de comunicado
    public static class ComunicadoItem {
        private String titulo;
        private String conteudo;
        private String data;
        private String remetente;

        public ComunicadoItem(String titulo, String conteudo, String data, String remetente) {
            this.titulo = titulo;
            this.conteudo = conteudo;
            this.data = data;
            this.remetente = remetente;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getConteudo() {
            return conteudo;
        }

        public String getData() {
            return data;
        }

        public String getRemetente() {
            return remetente;
        }
    }

    // Adapter para o RecyclerView de comunicados
    public class ComunicadosAdapter extends RecyclerView.Adapter<ComunicadosAdapter.ComunicadoViewHolder> {
        private List<ComunicadoItem> comunicadosList;

        public ComunicadosAdapter(List<ComunicadoItem> comunicadosList) {
            this.comunicadosList = comunicadosList;
        }

        @Override
        public ComunicadoViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_comunicado, parent, false);
            return new ComunicadoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ComunicadoViewHolder holder, int position) {
            ComunicadoItem item = comunicadosList.get(position);
            holder.txtTitulo.setText(item.getTitulo());
            holder.txtConteudo.setText(item.getConteudo());
            holder.txtData.setText("Data: " + item.getData());
            holder.txtRemetente.setText("De: " + item.getRemetente());
        }

        @Override
        public int getItemCount() {
            return comunicadosList.size();
        }

        public class ComunicadoViewHolder extends RecyclerView.ViewHolder {
            TextView txtTitulo, txtConteudo, txtData, txtRemetente;

            public ComunicadoViewHolder(View itemView) {
                super(itemView);
                txtTitulo = itemView.findViewById(R.id.txtTituloComunicado);
                txtConteudo = itemView.findViewById(R.id.txtConteudoComunicado);
                txtData = itemView.findViewById(R.id.txtDataComunicado);
                txtRemetente = itemView.findViewById(R.id.txtRemetenteComunicado);
            }
        }
    }
}


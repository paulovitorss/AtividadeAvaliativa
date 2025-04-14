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

public class FrequenciaActivity extends AppCompatActivity {
    private TextView txtTitulo;
    private RecyclerView recyclerViewFrequencia;
    private Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequencia);

        // Inicializar componentes
        txtTitulo = findViewById(R.id.txtTitulo);
        recyclerViewFrequencia = findViewById(R.id.recyclerViewFrequencia);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Configurar título
        txtTitulo.setText("Frequência Escolar - 2º Bimestre");

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
        // Dados simulados para a frequência
        List<FrequenciaItem> frequenciaList = new ArrayList<>();
        frequenciaList.add(new FrequenciaItem("Matemática", 20, 18, 90));
        frequenciaList.add(new FrequenciaItem("Português", 20, 19, 95));
        frequenciaList.add(new FrequenciaItem("Ciências", 20, 17, 85));
        frequenciaList.add(new FrequenciaItem("História", 20, 16, 80));
        frequenciaList.add(new FrequenciaItem("Geografia", 20, 18, 90));
        frequenciaList.add(new FrequenciaItem("Inglês", 20, 20, 100));
        frequenciaList.add(new FrequenciaItem("Educação Física", 20, 20, 100));
        frequenciaList.add(new FrequenciaItem("Artes", 20, 19, 95));

        // Configurar adapter e layout manager
        FrequenciaAdapter adapter = new FrequenciaAdapter(frequenciaList);
        recyclerViewFrequencia.setAdapter(adapter);
        recyclerViewFrequencia.setLayoutManager(new LinearLayoutManager(this));
    }

    // Classe modelo para os itens de frequência
    public static class FrequenciaItem {
        private String disciplina;
        private int totalAulas;
        private int aulasPresentes;
        private int porcentagem;

        public FrequenciaItem(String disciplina, int totalAulas, int aulasPresentes, int porcentagem) {
            this.disciplina = disciplina;
            this.totalAulas = totalAulas;
            this.aulasPresentes = aulasPresentes;
            this.porcentagem = porcentagem;
        }

        public String getDisciplina() {
            return disciplina;
        }

        public int getTotalAulas() {
            return totalAulas;
        }

        public int getAulasPresentes() {
            return aulasPresentes;
        }

        public int getPorcentagem() {
            return porcentagem;
        }
    }

    // Adapter para o RecyclerView de frequência
    public class FrequenciaAdapter extends RecyclerView.Adapter<FrequenciaAdapter.FrequenciaViewHolder> {
        private List<FrequenciaItem> frequenciaList;

        public FrequenciaAdapter(List<FrequenciaItem> frequenciaList) {
            this.frequenciaList = frequenciaList;
        }

        @Override
        public FrequenciaViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_frequencia, parent, false);
            return new FrequenciaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FrequenciaViewHolder holder, int position) {
            FrequenciaItem item = frequenciaList.get(position);
            holder.txtDisciplina.setText(item.getDisciplina());
            holder.txtTotalAulas.setText(String.valueOf(item.getTotalAulas()));
            holder.txtAulasPresentes.setText(String.valueOf(item.getAulasPresentes()));
            holder.txtPorcentagem.setText(item.getPorcentagem() + "%");

            // Definir cor com base na porcentagem
            if (item.getPorcentagem() < 75) {
                holder.txtPorcentagem.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                holder.txtPorcentagem.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }
        }

        @Override
        public int getItemCount() {
            return frequenciaList.size();
        }

        public class FrequenciaViewHolder extends RecyclerView.ViewHolder {
            TextView txtDisciplina, txtTotalAulas, txtAulasPresentes, txtPorcentagem;

            public FrequenciaViewHolder(View itemView) {
                super(itemView);
                txtDisciplina = itemView.findViewById(R.id.txtDisciplina);
                txtTotalAulas = itemView.findViewById(R.id.txtTotalAulas);
                txtAulasPresentes = itemView.findViewById(R.id.txtAulasPresentes);
                txtPorcentagem = itemView.findViewById(R.id.txtPorcentagem);
            }
        }
    }
}


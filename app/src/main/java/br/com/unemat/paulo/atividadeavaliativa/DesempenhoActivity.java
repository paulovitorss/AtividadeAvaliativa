package br.com.unemat.paulo.atividadeavaliativa;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.List;

import br.com.unemat.paulo.atividadeavaliativa.controller.DesempenhoController;
import br.com.unemat.paulo.atividadeavaliativa.model.Aluno;

public class DesempenhoActivity extends AppCompatActivity {
    private TextView txtMediaAtual;
    private TextView txtMediaAnterior;
    private TextView txtTendencia;

    DesempenhoController controller = new DesempenhoController(this);
    List<String> listaDisciplinas = controller.getDisciplinas();
    private String[] disciplinas = {"Matemática", "Português", "Ciências", "História", "Geografia", "Inglês", "Educação Física", "Artes"};
    private float[] mediasAtuais = {8.0f, 7.5f, 8.8f, 7.8f, 7.5f, 9.3f, 10.0f, 8.8f};
    private float[] mediasAnteriores = {7.5f, 8.0f, 8.5f, 7.0f, 8.0f, 9.0f, 9.5f, 8.5f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desempenho);

        // Inicializar componentes
        TextView txtTitulo = findViewById(R.id.txtTitulo);
        Spinner spinnerDisciplina = findViewById(R.id.spinnerDisciplina);
        txtMediaAtual = findViewById(R.id.txtMediaAtual);
        txtMediaAnterior = findViewById(R.id.txtMediaAnterior);
        txtTendencia = findViewById(R.id.txtTendencia);
        CardView cardMediaAtual = findViewById(R.id.cardMediaAtual);
        CardView cardMediaAnterior = findViewById(R.id.cardMediaAnterior);
        CardView cardTendencia = findViewById(R.id.cardTendencia);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        // Configurar título
        txtTitulo.setText("Análise de Desempenho");

        // Configurar spinner
        List<String> listaDisciplinas = controller.getDisciplinas();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaDisciplinas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisciplina.setAdapter(adapter);

        // Para carregar os alunos do banco, você pode fazer algo assim:
        List<Aluno> alunos = controller.getTodosAlunos();
        for (Aluno a : alunos) {
            Log.d("ALUNO", a.getNome() + " - " + a.getSerie());
        }

        // Configurar listener para o spinner
        spinnerDisciplina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atualizarDados(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não fazer nada
            }
        });

        // Configurar listener para o botão de voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Inicializar com a primeira disciplina
        atualizarDados(0);
    }

    private void atualizarDados(int position) {
        float mediaAtual = mediasAtuais[position];
        float mediaAnterior = mediasAnteriores[position];

        txtMediaAtual.setText(String.format("%.1f", mediaAtual));
        txtMediaAnterior.setText(String.format("%.1f", mediaAnterior));

        // Calcular tendência
        float diferenca = mediaAtual - mediaAnterior;
        String tendenciaTexto;
        int tendenciaCor;

        if (diferenca > 0.5f) {
            tendenciaTexto = "Melhora significativa";
            tendenciaCor = Color.parseColor("#4CAF50"); // Verde
        } else if (diferenca > 0) {
            tendenciaTexto = "Leve melhora";
            tendenciaCor = Color.parseColor("#8BC34A"); // Verde claro
        } else if (diferenca > -0.5f) {
            tendenciaTexto = "Estável";
            tendenciaCor = Color.parseColor("#FFC107"); // Amarelo
        } else {
            tendenciaTexto = "Queda no desempenho";
            tendenciaCor = Color.parseColor("#F44336"); // Vermelho
        }

        txtTendencia.setText(tendenciaTexto);
        txtTendencia.setTextColor(tendenciaCor);
    }
}


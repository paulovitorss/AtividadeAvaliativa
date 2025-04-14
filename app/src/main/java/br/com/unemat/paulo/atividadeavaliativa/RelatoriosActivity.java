package br.com.unemat.paulo.atividadeavaliativa;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

public class RelatoriosActivity extends AppCompatActivity {
    private Spinner spinnerTipoRelatorio;
    private Spinner spinnerFiltro;
    private TextView txtInfoGrafico;
    private TextView txtDadosRelatorio;

    private List<String> tiposRelatorio;
    private List<String> filtrosAlunos;
    private List<String> filtrosTurmas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);

        // Inicializar componentes
        TextView txtTitulo = findViewById(R.id.txtTitulo);
        spinnerTipoRelatorio = findViewById(R.id.spinnerTipoRelatorio);
        spinnerFiltro = findViewById(R.id.spinnerFiltro);
        CardView cardViewGrafico = findViewById(R.id.cardViewGrafico);
        txtInfoGrafico = findViewById(R.id.txtInfoGrafico);
        txtDadosRelatorio = findViewById(R.id.txtDadosRelatorio);
        Button btnGerarPDF = findViewById(R.id.btnGerarPDF);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        // Configurar título
        txtTitulo.setText("Relatórios");

        // Configurar dados simulados
        setupDadosSimulados();

        // Configurar spinner de tipo de relatório
        ArrayAdapter<String> tiposAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposRelatorio);
        tiposAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoRelatorio.setAdapter(tiposAdapter);

        // Configurar listener para o spinner de tipo de relatório
        spinnerTipoRelatorio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Atualizar spinner de filtro com base no tipo de relatório selecionado
                if (position == 0) { // Desempenho Individual
                    ArrayAdapter<String> filtroAdapter = new ArrayAdapter<>(RelatoriosActivity.this,
                            android.R.layout.simple_spinner_item, filtrosAlunos);
                    filtroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerFiltro.setAdapter(filtroAdapter);
                } else { // Desempenho por Turma ou Frequência por Turma
                    ArrayAdapter<String> filtroAdapter = new ArrayAdapter<>(RelatoriosActivity.this,
                            android.R.layout.simple_spinner_item, filtrosTurmas);
                    filtroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerFiltro.setAdapter(filtroAdapter);
                }

                // Atualizar dados do relatório
                atualizarDadosRelatorio();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não fazer nada
            }
        });

        // Configurar listener para o spinner de filtro
        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Atualizar dados do relatório
                atualizarDadosRelatorio();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não fazer nada
            }
        });

        // Configurar listener para o botão de gerar PDF
        btnGerarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simulação de geração de PDF
                String tipoRelatorio = spinnerTipoRelatorio.getSelectedItem().toString();
                String filtro = spinnerFiltro.getSelectedItem().toString();
                String mensagem = "Relatório de " + tipoRelatorio + " para " + filtro + " gerado com sucesso!";
                android.widget.Toast.makeText(RelatoriosActivity.this, mensagem, android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar listener para o botão de voltar
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupDadosSimulados() {
        // Lista de tipos de relatório
        tiposRelatorio = new ArrayList<>();
        tiposRelatorio.add("Desempenho Individual");
        tiposRelatorio.add("Desempenho por Turma");
        tiposRelatorio.add("Frequência por Turma");

        // Lista de filtros para alunos
        filtrosAlunos = new ArrayList<>();
        filtrosAlunos.add("Maria Silva - 8º Ano");
        filtrosAlunos.add("João Santos - 7º Ano");
        filtrosAlunos.add("Ana Oliveira - 9º Ano");
        filtrosAlunos.add("Pedro Costa - 6º Ano");
        filtrosAlunos.add("Luiza Fernandes - 8º Ano");

        // Lista de filtros para turmas
        filtrosTurmas = new ArrayList<>();
        filtrosTurmas.add("6º Ano");
        filtrosTurmas.add("7º Ano");
        filtrosTurmas.add("8º Ano");
        filtrosTurmas.add("9º Ano");
    }

    private void atualizarDadosRelatorio() {
        String tipoRelatorio = spinnerTipoRelatorio.getSelectedItem().toString();
        String filtro = spinnerFiltro.getSelectedItem().toString();

        // Atualizar informações do gráfico
        txtInfoGrafico.setText("Gráfico de " + tipoRelatorio + " - " + filtro);

        // Atualizar dados do relatório com base no tipo selecionado
        StringBuilder dadosBuilder = new StringBuilder();

        if (tipoRelatorio.equals("Desempenho Individual")) {
            dadosBuilder.append("Relatório de Desempenho Individual\n\n");
            dadosBuilder.append("Aluno: ").append(filtro).append("\n\n");
            dadosBuilder.append("Disciplina | Nota 1 | Nota 2 | Média\n");
            dadosBuilder.append("-------------------------------\n");
            dadosBuilder.append("Matemática | 8.5 | 7.5 | 8.0\n");
            dadosBuilder.append("Português | 7.0 | 8.0 | 7.5\n");
            dadosBuilder.append("Ciências | 9.0 | 8.5 | 8.8\n");
            dadosBuilder.append("História | 7.5 | 8.0 | 7.8\n");
            dadosBuilder.append("Geografia | 8.0 | 7.0 | 7.5\n");
            dadosBuilder.append("Inglês | 9.5 | 9.0 | 9.3\n");
            dadosBuilder.append("Educação Física | 10.0 | 10.0 | 10.0\n");
            dadosBuilder.append("Artes | 8.5 | 9.0 | 8.8\n\n");
            dadosBuilder.append("Média Geral: 8.5");
        } else if (tipoRelatorio.equals("Desempenho por Turma")) {
            dadosBuilder.append("Relatório de Desempenho por Turma\n\n");
            dadosBuilder.append("Turma: ").append(filtro).append("\n\n");
            dadosBuilder.append("Disciplina | Média da Turma\n");
            dadosBuilder.append("------------------------\n");
            dadosBuilder.append("Matemática | 7.8\n");
            dadosBuilder.append("Português | 8.1\n");
            dadosBuilder.append("Ciências | 8.5\n");
            dadosBuilder.append("História | 7.9\n");
            dadosBuilder.append("Geografia | 7.6\n");
            dadosBuilder.append("Inglês | 8.7\n");
            dadosBuilder.append("Educação Física | 9.5\n");
            dadosBuilder.append("Artes | 8.9\n\n");
            dadosBuilder.append("Média Geral da Turma: 8.4");
        } else { // Frequência por Turma
            dadosBuilder.append("Relatório de Frequência por Turma\n\n");
            dadosBuilder.append("Turma: ").append(filtro).append("\n\n");
            dadosBuilder.append("Disciplina | % Média de Presença\n");
            dadosBuilder.append("----------------------------\n");
            dadosBuilder.append("Matemática | 92%\n");
            dadosBuilder.append("Português | 94%\n");
            dadosBuilder.append("Ciências | 90%\n");
            dadosBuilder.append("História | 88%\n");
            dadosBuilder.append("Geografia | 91%\n");
            dadosBuilder.append("Inglês | 95%\n");
            dadosBuilder.append("Educação Física | 98%\n");
            dadosBuilder.append("Artes | 93%\n\n");
            dadosBuilder.append("Média Geral de Presença: 93%");
        }

        txtDadosRelatorio.setText(dadosBuilder.toString());
    }
}
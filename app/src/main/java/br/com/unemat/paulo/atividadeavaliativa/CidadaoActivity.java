package br.com.unemat.paulo.atividadeavaliativa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import br.com.unemat.paulo.atividadeavaliativa.ui.auth.LoginActivity;

public class CidadaoActivity extends AppCompatActivity {
    private TextView txtNomeAluno;
    private CardView cardBoletim;
    private CardView cardFrequencia;
    private CardView cardDesempenho;
    private CardView cardComunicados;
    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cidadao);

        // Inicializar componentes
        txtNomeAluno = findViewById(R.id.txtNomeAluno);
        cardBoletim = findViewById(R.id.cardBoletim);
        cardFrequencia = findViewById(R.id.cardFrequencia);
        cardDesempenho = findViewById(R.id.cardDesempenho);
        cardComunicados = findViewById(R.id.cardComunicados);
        btnSair = findViewById(R.id.btnSair);

        // Configurar nome do aluno (simulado)
        txtNomeAluno.setText("Pedro Paulo - 1º Ano");

        // Configurar listeners para os cards
        cardBoletim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CidadaoActivity.this, BoletimActivity.class);
                startActivity(intent);
            }
        });

        cardFrequencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CidadaoActivity.this, FrequenciaActivity.class);
                startActivity(intent);
            }
        });

        cardDesempenho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CidadaoActivity.this, DesempenhoActivity.class);
                startActivity(intent);
            }
        });

        cardComunicados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CidadaoActivity.this, ComunicadosActivity.class);
                startActivity(intent);
            }
        });

        // Configurar listener para o botão de sair
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CidadaoActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
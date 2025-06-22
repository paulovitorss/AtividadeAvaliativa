package br.com.unemat.paulo.atividadeavaliativa;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import br.com.unemat.paulo.atividadeavaliativa.ui.auth.LoginActivity;
import br.com.unemat.paulo.atividadeavaliativa.view.GerenciarAlunosActivity;
import br.com.unemat.paulo.atividadeavaliativa.view.GerenciarFrequenciaActivity;
import br.com.unemat.paulo.atividadeavaliativa.view.GerenciarNotasActivity;

public class AdminActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Inicializar a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Drawer e NavigationView
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);

        // Botão de menu (três risquinhos)
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Itens do menu lateral
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_perfil) {
                    // abrir tela de perfil
                    startActivity(new Intent(AdminActivity.this, PerfilActivity.class));
                } else if (id == R.id.nav_sair) {
                    // ação de logout
                    Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Inicializar cards e botão
        CardView cardGerenciarAlunos = findViewById(R.id.cardGerenciarAlunos);
        CardView cardGerenciarNotas = findViewById(R.id.cardGerenciarNotas);
        CardView cardGerenciarFrequencia = findViewById(R.id.cardGerenciarFrequencia);
        CardView cardEnviarComunicados = findViewById(R.id.cardEnviarComunicados);
        CardView cardRelatorios = findViewById(R.id.cardRelatorios);
        Button btnSair = findViewById(R.id.btnSair);

        // Listeners
        cardGerenciarAlunos.setOnClickListener(v -> startActivity(new Intent(this, GerenciarAlunosActivity.class)));
        cardGerenciarNotas.setOnClickListener(v -> startActivity(new Intent(this, GerenciarNotasActivity.class)));
        cardGerenciarFrequencia.setOnClickListener(v -> startActivity(new Intent(this, GerenciarFrequenciaActivity.class)));
        cardEnviarComunicados.setOnClickListener(v -> startActivity(new Intent(this, EnviarComunicadosActivity.class)));
        cardRelatorios.setOnClickListener(v -> startActivity(new Intent(this, RelatoriosActivity.class)));

        btnSair.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}

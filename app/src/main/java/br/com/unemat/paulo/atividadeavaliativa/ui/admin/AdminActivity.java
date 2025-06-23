package br.com.unemat.paulo.atividadeavaliativa.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import br.com.unemat.paulo.atividadeavaliativa.ui.auth.LoginActivity;
import br.com.unemat.paulo.atividadeavaliativa.view.GerenciarAlunosActivity;
import br.com.unemat.paulo.atividadeavaliativa.view.GerenciarFrequenciaActivity;
import br.com.unemat.paulo.atividadeavaliativa.view.GerenciarNotasActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TokenManager tokenManager;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        tokenManager = TokenManager.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_perfil) {
                // startActivity(new Intent(AdminActivity.this, PerfilActivity.class));
            } else if (id == R.id.nav_sair) {
                logout();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        CardView cardGerenciarAlunos = findViewById(R.id.cardGerenciarAlunos);
        CardView cardGerenciarNotas = findViewById(R.id.cardGerenciarNotas);
        CardView cardGerenciarFrequencia = findViewById(R.id.cardGerenciarFrequencia);
        // CardView cardEnviarComunicados = findViewById(R.id.cardEnviarComunicados);
        // CardView cardRelatorios = findViewById(R.id.cardRelatorios);
        Button btnSair = findViewById(R.id.btnSair);

        cardGerenciarAlunos.setOnClickListener(v -> startActivity(new Intent(this, GerenciarAlunosActivity.class)));
        cardGerenciarNotas.setOnClickListener(v -> startActivity(new Intent(this, GerenciarNotasActivity.class)));
        cardGerenciarFrequencia.setOnClickListener(v -> startActivity(new Intent(this, GerenciarFrequenciaActivity.class)));
        // cardEnviarComunicados.setOnClickListener(v -> startActivity(new Intent(this, EnviarComunicadosActivity.class)));
        // cardRelatorios.setOnClickListener(v -> startActivity(new Intent(this, RelatoriosActivity.class)));

        btnSair.setOnClickListener(v -> logout());
    }

    private void logout() {
        disposables.add(tokenManager.clearToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::navigateToLoginScreen,
                        throwable -> navigateToLoginScreen()
                )
        );
    }

    private void navigateToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
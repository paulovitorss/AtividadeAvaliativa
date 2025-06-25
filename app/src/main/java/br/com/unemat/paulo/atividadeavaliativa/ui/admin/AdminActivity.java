package br.com.unemat.paulo.atividadeavaliativa.ui.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

import br.com.unemat.paulo.atividadeavaliativa.EnviarComunicadosActivity;
import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.RelatoriosActivity;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import br.com.unemat.paulo.atividadeavaliativa.ui.auth.LoginActivity;
import br.com.unemat.paulo.atividadeavaliativa.ui.base.BaseActivity;
import br.com.unemat.paulo.atividadeavaliativa.view.GerenciarAlunosActivity;
import br.com.unemat.paulo.atividadeavaliativa.view.GerenciarFrequenciaActivity;
import br.com.unemat.paulo.atividadeavaliativa.view.GerenciarNotasActivity;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class AdminActivity extends BaseActivity {

    private TokenManager tokenManager;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_admin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenManager = TokenManager.getInstance(this);

        setupClickListeners();
    }

    private void setupClickListeners() {
        CardView cardGerenciarAlunos = findViewById(R.id.cardGerenciarAlunos);
        CardView cardGerenciarNotas = findViewById(R.id.cardGerenciarNotas);
        CardView cardGerenciarFrequencia = findViewById(R.id.cardGerenciarFrequencia);
        CardView cardEnviarComunicados = findViewById(R.id.cardEnviarComunicados);
        CardView cardRelatorios = findViewById(R.id.cardRelatorios);

        cardGerenciarAlunos.setOnClickListener(v -> startActivity(new Intent(this, GerenciarAlunosActivity.class)));
        cardGerenciarNotas.setOnClickListener(v -> startActivity(new Intent(this, GerenciarNotasActivity.class)));
        cardGerenciarFrequencia.setOnClickListener(v -> startActivity(new Intent(this, GerenciarFrequenciaActivity.class)));
        cardEnviarComunicados.setOnClickListener(v -> startActivity(new Intent(this, EnviarComunicadosActivity.class)));
        cardRelatorios.setOnClickListener(v -> startActivity(new Intent(this, RelatoriosActivity.class)));
    }

    @Override
    protected void logout() {
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
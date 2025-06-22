package br.com.unemat.paulo.atividadeavaliativa.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import br.com.unemat.paulo.atividadeavaliativa.CidadaoActivity;
import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextSenha;
    private LoginViewModel loginViewModel;
    private Button btnEntrar;
    private ProgressBar progressBar;
    private TokenManager tokenManager;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        tokenManager = TokenManager.getInstance(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        progressBar = findViewById(R.id.progressBar);

        btnEntrar.setOnClickListener(v -> handleLogin());

        checkIfAlreadyLoggedIn();
    }

    private void checkIfAlreadyLoggedIn() {
        disposables.add(tokenManager.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(token -> {
                    if (token != null && !token.isEmpty()) {
                        navigateToMainScreen();
                    }
                }, throwable -> {
                    Toast.makeText(this, "Erro ao verificar sessão", Toast.LENGTH_SHORT).show();
                })
        );
    }

    private void handleLogin() {
        String username = editTextEmail.getText().toString().trim();
        String password = editTextSenha.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        loginViewModel.login(username, password).observe(this, loginResponse -> {
            if (loginResponse != null && loginResponse.getAccessToken() != null) {
                Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

                disposables.add(tokenManager.saveToken(loginResponse.getAccessToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            setLoading(false);
                            navigateToMainScreen();
                        }, throwable -> {
                            setLoading(false);
                            Toast.makeText(this, "Erro ao salvar sessão", Toast.LENGTH_SHORT).show();
                        })
                );

            } else {
                setLoading(false);
                Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(LoginActivity.this, CidadaoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnEntrar.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnEntrar.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
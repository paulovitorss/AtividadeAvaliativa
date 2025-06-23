package br.com.unemat.paulo.atividadeavaliativa.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.AdminActivity;
import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.security.JwtUtils;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import br.com.unemat.paulo.atividadeavaliativa.ui.user.UserActivity;
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

        initViews();
        tokenManager = TokenManager.getInstance(this);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        btnEntrar.setOnClickListener(v -> handleLogin());

        checkIfAlreadyLoggedIn();
    }

    private void checkIfAlreadyLoggedIn() {
        disposables.add(tokenManager.getToken()
                .filter(token -> !token.isEmpty())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::navigateToProperScreen,
                        throwable -> Log.e("LoginActivity", "Error checking for existing token", throwable)
                )
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
                String token = loginResponse.getAccessToken();

                disposables.add(tokenManager.saveToken(token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            setLoading(false);
                            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                            navigateToProperScreen(token);
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

    private void navigateToProperScreen(String token) {
        if (JwtUtils.hasRole(token, "ADMIN")) {
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            UUID userId = JwtUtils.getUserIdFromToken(token);
            if (userId != null) {
                Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                intent.putExtra("USER_ID_EXTRA", userId.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Token inválido. Faça login novamente.", Toast.LENGTH_SHORT).show();
                setLoading(false);
            }
        }
        finish();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        progressBar = findViewById(R.id.progressBar);
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
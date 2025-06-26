package br.com.unemat.paulo.atividadeavaliativa.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.data.model.User;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import br.com.unemat.paulo.atividadeavaliativa.ui.auth.LoginActivity;
import br.com.unemat.paulo.atividadeavaliativa.ui.base.BaseActivity;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class PerfilActivity extends BaseActivity {
    private TextView txtNome, txtEmail;
    private ProgressBar progressBar;

    private PerfilViewModel perfilViewModel;
    private TokenManager tokenManager;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_perfil;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        tokenManager = TokenManager.getInstance(this);

        initViews();

        observeProfileState();
    }

    private void initViews() {
        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void logout() {
        disposables.add(tokenManager.clearToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::navigateToLoginScreen)
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

    private void observeProfileState() {
        perfilViewModel.userUiState.observe(this, state -> {
            setLoading(state instanceof PerfilViewModel.UserUiState.Loading);

            if (state instanceof PerfilViewModel.UserUiState.Success) {
                User user = ((PerfilViewModel.UserUiState.Success) state).user;
                populateUi(user);
            } else if (state instanceof PerfilViewModel.UserUiState.Error) {
                String message = ((PerfilViewModel.UserUiState.Error) state).message;
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void populateUi(User user) {
        if (user != null) {
            txtNome.setText(user.getName());
            txtEmail.setText(user.getEmail());
        }
    }

    private void setLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }

        findViewById(R.id.card_profile_info).setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
    }
}
package br.com.unemat.paulo.atividadeavaliativa.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import br.com.unemat.paulo.atividadeavaliativa.data.model.User;

import br.com.unemat.paulo.atividadeavaliativa.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PerfilActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtNome, txtEmail;
    private ProgressBar progressBar;

    private PerfilViewModel perfilViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        initViews();
        setupToolbar();

        observeProfileState();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbarPerfil);
        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Meu Perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
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

        txtNome.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
        txtEmail.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
    }
}
package br.com.unemat.paulo.atividadeavaliativa.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.data.model.User;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import br.com.unemat.paulo.atividadeavaliativa.ui.auth.LoginActivity;
import br.com.unemat.paulo.atividadeavaliativa.ui.grade.GradeActivity;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Activity que exibe a tela principal do usuário.
 * Anotada com @AndroidEntryPoint para que o Hilt possa injetar suas dependências,
 * como o UserViewModel.
 */
@AndroidEntryPoint
public class UserActivity extends AppCompatActivity {
    private TextView txtNomeAluno;
    private CardView cardBoletim;
    private Button btnSair;
    private ProgressBar progressBar; // Adicionado para feedback de carregamento

    private UserViewModel userViewModel;
    private TokenManager tokenManager;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private UUID userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initViews();
        tokenManager = TokenManager.getInstance(this);
        // Hilt irá prover a instância correta do ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Extrai o ID do usuário do Intent
        if (!getUserIdFromIntent()) {
            // Se não for possível obter o ID, faz logout.
            Toast.makeText(this, "Erro: ID de usuário inválido.", Toast.LENGTH_LONG).show();
            logout();
            return;
        }

        // Configura os observadores e listeners
        observeUserState();
        setupClickListeners();

        // Inicia a busca pelos dados do usuário
        userViewModel.fetchUserById(userId);
    }

    private void initViews() {
        txtNomeAluno = findViewById(R.id.txtNomeAluno);
        cardBoletim = findViewById(R.id.cardBoletim);
        btnSair = findViewById(R.id.btnSair);
        // Encontre o ProgressBar no seu layout (assumindo que ele exista)
        progressBar = findViewById(R.id.progressBar);
    }

    private boolean getUserIdFromIntent() {
        String userIdString = getIntent().getStringExtra("USER_ID_EXTRA");
        if (userIdString != null && !userIdString.isEmpty()) {
            try {
                this.userId = UUID.fromString(userIdString);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Observa o LiveData de estado da UI do UserViewModel.
     * Este é o metodo reativo que atualiza a tela conforme o estado muda.
     */
    private void observeUserState() {
        userViewModel.userUiState.observe(this, state -> {
            setLoading(state instanceof UserViewModel.UserUiState.Loading);

            if (state instanceof UserViewModel.UserUiState.Success) {
                User user = ((UserViewModel.UserUiState.Success) state).user;
                updateUiWithUserData(user);
            } else if (state instanceof UserViewModel.UserUiState.Error) {
                String message = ((UserViewModel.UserUiState.Error) state).message;
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                logout(); // Se não conseguir carregar dados do usuário, faz logout
            }
        });
    }

    /**
     * Atualiza os componentes da UI com os dados do usuário.
     *
     * @param user O objeto User recebido do ViewModel.
     */
    private void updateUiWithUserData(User user) {
        if (user != null) {
            String welcomeText = "Olá, " + user.getName();
            txtNomeAluno.setText(welcomeText);
        }
    }

    private void setLoading(boolean isLoading) {
        // Exibe o ProgressBar e esconde o conteúdo principal enquanto carrega
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    private void setupClickListeners() {
        btnSair.setOnClickListener(v -> logout());

        cardBoletim.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, GradeActivity.class);
            // Passa o ID do estudante para a próxima tela
            intent.putExtra("USER_ID_EXTRA", userId.toString());
            startActivity(intent);
        });
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
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
    private ProgressBar progressBar;

    private UserViewModel userViewModel;
    private TokenManager tokenManager;
    private User currentUser;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initViews();
        tokenManager = TokenManager.getInstance(this);
        // Hilt irá prover a instância correta do ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Configura os observadores e listeners
        observeUserState();
        setupClickListeners();

        // Inicia a busca pelos dados do usuário
        userViewModel.fetchMyProfile();
    }

    private void initViews() {
        txtNomeAluno = findViewById(R.id.txtNomeAluno);
        cardBoletim = findViewById(R.id.cardBoletim);
        btnSair = findViewById(R.id.btnSair);
        progressBar = findViewById(R.id.progressBar);
    }

    /**
     * Observa o LiveData de estado da UI do UserViewModel.
     * Este é o metodo reativo que atualiza a tela conforme o estado muda.
     */
    private void observeUserState() {
        userViewModel.userUiState.observe(this, state -> {
            setLoading(state instanceof UserViewModel.UserUiState.Loading);

            if (state instanceof UserViewModel.UserUiState.Success) {
                this.currentUser = ((UserViewModel.UserUiState.Success) state).user;
                updateUiWithUserData(this.currentUser);
            } else if (state instanceof UserViewModel.UserUiState.Error) {
                String message = ((UserViewModel.UserUiState.Error) state).message;
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                logout();
            }
        });
    }

    /**
     * Atualiza os componentes da UI com os dados do usuário.
     *
     * @param user O objeto User recebido do ViewModel.
     */
    private void updateUiWithUserData(User user) {
        if (user == null) return;

        String welcomeText;

        // 1. Verifica de forma segura se o usuário tem o papel "STUDENT"
        boolean isStudent = user.getRoles() != null && user.getRoles().stream()
                .anyMatch(role -> "STUDENT".equals(role.getName()));

        // 2. Constrói a string de saudação com base na condição
        if (isStudent && user.getSeries() != null && !user.getSeries().isEmpty()) {
            // Formato para estudantes: "Olá, [Nome] - [Série]"
            welcomeText = "Olá, " + user.getName() + " - " + user.getSeries();
        } else {
            // Formato padrão para todos os outros usuários: "Olá, [Nome]"
            welcomeText = "Olá, " + user.getName();
        }

        // 3. Define o texto no TextView
        txtNomeAluno.setText(welcomeText);
    }

    private void setLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    private void setupClickListeners() {
        btnSair.setOnClickListener(v -> logout());

        cardBoletim.setOnClickListener(v -> {
            if (currentUser != null) {
                Intent intent = new Intent(UserActivity.this, GradeActivity.class);
                intent.putExtra("USER_ID_EXTRA", currentUser.getUserId().toString());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Aguarde o carregamento dos dados.", Toast.LENGTH_SHORT).show();
            }
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
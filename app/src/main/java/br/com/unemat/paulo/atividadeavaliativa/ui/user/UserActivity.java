package br.com.unemat.paulo.atividadeavaliativa.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import br.com.unemat.paulo.atividadeavaliativa.ui.auth.LoginActivity;
import br.com.unemat.paulo.atividadeavaliativa.ui.grade.GradeActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserActivity extends AppCompatActivity {
    private TextView txtNomeAluno;
    private CardView cardBoletim;
    private Button btnSair;
    private UserViewModel userViewModel;
    private TokenManager tokenManager;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initViews();
        tokenManager = TokenManager.getInstance(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        String userIdString = getIntent().getStringExtra("USER_ID_EXTRA");
        UUID userId = null;

        if (userIdString != null && !userIdString.isEmpty()) {
            try {
                userId = UUID.fromString(userIdString);
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Erro: ID de usuário inválido.", Toast.LENGTH_LONG).show();
                logout();
                return;
            }
        }

        if (userId == null) {
            Toast.makeText(this, "Erro: ID de usuário não encontrado.", Toast.LENGTH_LONG).show();
            logout();
            return;
        }

        loadUserData(userId);
        setupClickListeners();
    }

    private void initViews() {
        txtNomeAluno = findViewById(R.id.txtNomeAluno);
        cardBoletim = findViewById(R.id.cardBoletim);
        CardView cardFrequencia = findViewById(R.id.cardFrequencia);
        CardView cardDesempenho = findViewById(R.id.cardDesempenho);
        CardView cardComunicados = findViewById(R.id.cardComunicados);
        btnSair = findViewById(R.id.btnSair);
    }

    private void loadUserData(UUID userId) {
        userViewModel.getUser(userId).observe(this, user -> {
            if (user != null) {
                txtNomeAluno.setText(user.getName() + " — " + user.getUsername());
            } else {
                Toast.makeText(this, "Erro ao carregar dados do usuário.", Toast.LENGTH_SHORT).show();
                logout();
            }
        });
    }

    private void setupClickListeners() {
        btnSair.setOnClickListener(v -> logout());

        cardBoletim.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, GradeActivity.class);
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
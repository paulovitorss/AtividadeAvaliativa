package br.com.unemat.paulo.atividadeavaliativa.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.data.model.User;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import br.com.unemat.paulo.atividadeavaliativa.ui.auth.LoginActivity;
import br.com.unemat.paulo.atividadeavaliativa.ui.base.BaseActivity;
import br.com.unemat.paulo.atividadeavaliativa.ui.grade.GradeActivity;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class UserActivity extends BaseActivity {

    private TextView txtNomeAluno;
    private CardView cardBoletim;
    private ProgressBar progressBar;

    private UserViewModel userViewModel;
    private TokenManager tokenManager;
    private User currentUser;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        tokenManager = TokenManager.getInstance(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        observeUserState();
        setupClickListeners();

        userViewModel.fetchMyProfile();
    }

    private void initViews() {
        txtNomeAluno = findViewById(R.id.txtNomeAluno);
        cardBoletim = findViewById(R.id.cardBoletim);
        progressBar = findViewById(R.id.progressBar);
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

    private void setupClickListeners() {
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

    private void updateUiWithUserData(User user) {
        if (user == null) return;
        String welcomeText;
        boolean isStudent = user.getRoles() != null && user.getRoles().stream()
                .anyMatch(role -> "STUDENT".equals(role.getName()));
        if (isStudent && user.getSeries() != null && !user.getSeries().isEmpty()) {
            welcomeText = "Olá, " + user.getName() + " - " + user.getSeries();
        } else {
            welcomeText = "Olá, " + user.getName();
        }
        txtNomeAluno.setText(welcomeText);
    }

    private void setLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

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
}
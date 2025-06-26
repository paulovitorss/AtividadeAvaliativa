package br.com.unemat.paulo.atividadeavaliativa.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import java.util.stream.Collectors;

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
    private TextView txtNome, txtEmail, txtUsername, txtCpf, txtDataNascimento;

    private CardView cardStudentInfo;

    private TextView txtSeries, txtRegistration;

    private CardView cardGuardianInfo;

    private TextView txtPhoneNumber, txtStudentsList;

    private ProgressBar progressBar;

    private View contentScrollView;

    private PerfilViewModel perfilViewModel;

    private TokenManager tokenManager;

    private CardView cardStudentGuardianList;

    private TextView txtGuardiansList;

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
        txtUsername = findViewById(R.id.txtUsername);
        txtCpf = findViewById(R.id.txtCpf);
        txtDataNascimento = findViewById(R.id.txtDataNascimento);
        cardStudentInfo = findViewById(R.id.card_student_info);
        txtSeries = findViewById(R.id.txtSeries);
        txtRegistration = findViewById(R.id.txtRegistration);
        cardGuardianInfo = findViewById(R.id.card_guardian_info);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtStudentsList = findViewById(R.id.txtStudentsList);
        progressBar = findViewById(R.id.progressBar);
        contentScrollView = findViewById(R.id.content_scrollview);
        cardStudentGuardianList = findViewById(R.id.card_student_guardian_list);
        txtGuardiansList = findViewById(R.id.txtGuardiansList);
    }

    private void observeProfileState() {
        perfilViewModel.userUiState.observe(this, state -> {
            setLoading(state instanceof PerfilViewModel.UserUiState.Loading);

            if (state instanceof PerfilViewModel.UserUiState.Success) {
                populateUi(((PerfilViewModel.UserUiState.Success) state).user);
            } else if (state instanceof PerfilViewModel.UserUiState.Error) {
                Toast.makeText(this, ((PerfilViewModel.UserUiState.Error) state).message, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void populateUi(User user) {
        if (user == null) return;

        txtNome.setText("Nome: " + user.getName());
        txtEmail.setText("Email: " + user.getEmail());
        txtUsername.setText("Usuário: " + user.getUsername());
        txtCpf.setText("CPF: " + user.getCpf());
        txtDataNascimento.setText("Nascimento: " + user.getDateOfBirth());

        if (user.getSeries() != null) {
            cardStudentInfo.setVisibility(View.VISIBLE);
            txtSeries.setText("Série: " + user.getSeries());
            txtRegistration.setText("Matrícula: " + user.getRegistrationNumber());
        }

        if (user.getGuardians() != null && !user.getGuardians().isEmpty()) {
            cardStudentGuardianList.setVisibility(View.VISIBLE);

            String guardiansInfo = user.getGuardians().stream()
                    .map(guardian -> "- " + guardian.getName() + "\n  Telefone: " + (guardian.getPhoneNumber() != null ? guardian.getPhoneNumber() : "Não informado"))
                    .collect(Collectors.joining("\n\n"));

            txtGuardiansList.setText(guardiansInfo);
        }

        if (user.getPhoneNumber() != null) {
            cardGuardianInfo.setVisibility(View.VISIBLE);
            txtPhoneNumber.setText("Telefone: " + user.getPhoneNumber());

            if (user.getStudents() != null && !user.getStudents().isEmpty()) {
                String studentsNames = user.getStudents().stream()
                        .map(student -> "- " + student.getName())
                        .collect(Collectors.joining("\n"));
                txtStudentsList.setText(studentsNames);
                txtStudentsList.setVisibility(View.VISIBLE);
                findViewById(R.id.label_students).setVisibility(View.VISIBLE);
            }
        }
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        contentScrollView.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
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
}
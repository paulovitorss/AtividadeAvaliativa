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

import br.com.unemat.paulo.atividadeavaliativa.R;
import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginResponse;
import br.com.unemat.paulo.atividadeavaliativa.ui.admin.AdminActivity;
import br.com.unemat.paulo.atividadeavaliativa.ui.user.UserActivity;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity de autenticação:
 * <ul>
 *   <li>Infla o layout de login;</li>
 *   <li>Inicializa as Views (email, senha, botão, progressbar);</li>
 *   <li>Obtém {@link LoginViewModel} via Hilt;</li>
 *   <li>Dispara o fluxo de login e observa {@code loginUiState};</li>
 *   <li>Exibe feedback (Loading, Success, Error) e navega para tela correta.</li>
 * </ul>
 *
 * <p><strong>Boas práticas:</strong>
 * <ul>
 *   <li>Substituir {@code findViewById} por ViewBinding ou DataBinding para eliminar boilerplate e NPEs.</li>
 *   <li>Centralizar strings em {@code strings.xml} em vez de hard-code.</li>
 *   <li>Usar Navigation Component + Safe Args para navegação tipada e gerenciamento de back stack.</li>
 *   <li>Evitar Toasts diretos para eventos one-shot; usar padrão Event/SingleLiveEvent.</li>
 *   <li>Tratamento de erros uniformizado (por ex. mostrar diálogos vs. toasts).</li>
 * </ul>
 * </p>
 */
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button btnEntrar;
    private ProgressBar progressBar;

    // ViewModel injetado com Hilt
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        btnEntrar.setOnClickListener(v -> handleLoginClick());
        loginViewModel.loginUiState.observe(this, this::handleLoginState);
    }

    /**
     * Inicializa referências de Views.
     * <p><i>Melhorar:</i> usar ViewBinding para evitar chamadas manuais.</p>
     */
    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        progressBar = findViewById(R.id.progressBar);
    }

    /**
     * Valida campos e invoca o metodo de login do ViewModel.
     * <p><i>Atenção:</i> sempre faça trim() e valide emptiness antes de chamar a API.</p>
     */
    private void handleLoginClick() {
        String username = editTextEmail.getText().toString().trim();
        String password = editTextSenha.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_preencha_campos), Toast.LENGTH_SHORT).show();
            return;
        }

        loginViewModel.login(username, password);
    }

    /**
     * Lida com os estados emitidos por {@link LoginViewModel.LoginUiState}:
     * <ul>
     *   <li>Loading: mostra ProgressBar e desabilita botão;</li>
     *   <li>Success: oculta ProgressBar, exibe Toast e navega;</li>
     *   <li>Error: oculta ProgressBar e exibe mensagem de erro.</li>
     * </ul>
     *
     * @param state estado atual da UI de login
     */
    private void handleLoginState(LoginViewModel.LoginUiState state) {
        if (state instanceof LoginViewModel.LoginUiState.Loading) {
            setLoading(true);

        } else if (state instanceof LoginViewModel.LoginUiState.Success) {
            setLoading(false);
            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

            LoginResponse loginData = ((LoginViewModel.LoginUiState.Success) state).data;
            navigateToProperScreen(loginData);

        } else if (state instanceof LoginViewModel.LoginUiState.Error) {
            setLoading(false);
            String errorMessage = ((LoginViewModel.LoginUiState.Error) state).message;
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Atualiza visibilidade de ProgressBar e habilita/desabilita botão.
     *
     * @param isLoading indica se estamos em carregamento
     */
    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnEntrar.setEnabled(!isLoading);
    }

    /**
     * Decide para qual Activity navegar com base no papel do usuário:
     * <ul>
     *   <li>ADMIN ou TEACHER → {@link AdminActivity};</li>
     *   <li>outros → {@link UserActivity} (passando USER_ID_EXTRA).</li>
     * </ul>
     * <p>
     * Usa flags para limpar a pilha de Activities e evitar voltar ao login.
     * </p>
     *
     * @param loginData resposta de login contendo roles e userId
     */
    private void navigateToProperScreen(LoginResponse loginData) {
        Intent intent;
        String role = loginData.getRole();
        String userId = loginData.getUserId();

        if ("ADMIN".equals(role) || "TEACHER".equals(role)) {
            intent = new Intent(this, AdminActivity.class);
        } else {
            intent = new Intent(this, UserActivity.class);
            intent.putExtra("USER_ID_EXTRA", userId);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
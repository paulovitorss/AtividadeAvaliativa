package br.com.unemat.paulo.atividadeavaliativa.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginResponse;
import br.com.unemat.paulo.atividadeavaliativa.data.repository.AuthRepository;
import br.com.unemat.paulo.atividadeavaliativa.domain.usecase.LoginUseCase;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel de login responsável por:
 * <ul>
 *   <li>orquestrar o fluxo de autenticação via {@link LoginUseCase};</li>
 *   <li>gerenciar o estado de UI representado por {@link LoginUiState};</li>
 *   <li>persistir o token retornado chamando {@link AuthRepository}.</li>
 * </ul>
 *
 * <p>
 * Mantém a UI desacoplada da camada de dados e insere lógica de threading,
 * garantindo que chamadas de rede não bloqueiem a thread principal.
 * </p>
 */
@HiltViewModel
public class LoginViewModel extends ViewModel {

    // Caso de uso que encapsula a lógica de requisição de login
    private final LoginUseCase loginUseCase;
    // Repositório para persistência de token
    private final AuthRepository authRepository;

    // Estado interno mutável (privado)
    private final MutableLiveData<LoginUiState> _loginUiState = new MutableLiveData<>();
    // Exposição imutável para a Activity/Fragment
    public final LiveData<LoginUiState> loginUiState = _loginUiState;

    /**
     * Injeta o caso de uso e o repositório de autenticação.
     *
     * @param loginUseCase   fluxo de negócio para autenticação
     * @param authRepository responsável por salvar o token JWT
     */
    @Inject
    public LoginViewModel(LoginUseCase loginUseCase,
                          AuthRepository authRepository) {
        this.loginUseCase = loginUseCase;
        this.authRepository = authRepository;
    }

    /**
     * Dispara o processo de login:
     * <ol>
     *   <li>configura o estado para Loading;</li>
     *   <li>executa requisição via Retrofit;</li>
     *   <li>em caso de sucesso, persiste o token em background e emite Success;</li>
     *   <li>em caso de erro, emite Error com mensagem adequada.</li>
     * </ol>
     *
     * @param username usuário ou e-mail
     * @param password senha em texto puro
     */
    public void login(String username, String password) {
        _loginUiState.setValue(new LoginUiState.Loading());

        loginUseCase.invoke(username, password)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call,
                                           @NonNull Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            String token = loginResponse.getAccessToken();

                            // Persiste token em background para não travar a UI
                            new Thread(() -> authRepository.saveToken(token)).start();

                            // Atualiza o estado com os dados de sucesso
                            _loginUiState.postValue(new LoginUiState.Success(loginResponse));
                        } else {
                            // Erro de credenciais ou resposta inválida
                            _loginUiState.postValue(
                                    new LoginUiState.Error("Usuário ou senha inválidos"));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call,
                                          @NonNull Throwable t) {
                        // Falha de rede, timeout etc.
                        _loginUiState.postValue(
                                new LoginUiState.Error("Erro de conexão. Tente novamente."));
                    }
                });
    }

    /**
     * Estados possíveis da UI de login.
     * <ul>
     *   <li>{@link Loading}: operação em andamento.</li>
     *   <li>{@link Success}: login concluído com {@link LoginResponse}.</li>
     *   <li>{@link Error}: falha, com mensagem de erro.</li>
     * </ul>
     */
    public static abstract class LoginUiState {
        private LoginUiState() {
        }

        /**
         * Indica que a requisição está em progresso.
         */
        public static final class Loading extends LoginUiState {
        }

        /**
         * Indica que o login foi bem-sucedido.
         */
        public static final class Success extends LoginUiState {
            public final LoginResponse data;

            public Success(LoginResponse data) {
                this.data = data;
            }
        }

        /**
         * Indica que houve erro e carrega mensagem para exibição.
         */
        public static final class Error extends LoginUiState {
            public final String message;

            public Error(String message) {
                this.message = message;
            }
        }
    }
}
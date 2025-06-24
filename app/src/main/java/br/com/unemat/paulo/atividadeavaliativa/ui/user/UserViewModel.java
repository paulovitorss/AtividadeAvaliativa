package br.com.unemat.paulo.atividadeavaliativa.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.UUID;

import javax.inject.Inject;

import br.com.unemat.paulo.atividadeavaliativa.data.model.User;
import br.com.unemat.paulo.atividadeavaliativa.data.repository.UserRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel para a tela que exibe os detalhes de um Usuário.
 * Anotado com @HiltViewModel para que suas dependências sejam injetadas pelo Hilt.
 */
@HiltViewModel
public class UserViewModel extends ViewModel {

    private final UserRepository userRepository;

    // LiveData privado para controlar o estado da UI.
    private final MutableLiveData<UserUiState> _userUiState = new MutableLiveData<>();
    // LiveData público para a UI (Activity/Fragment) observar.
    public final LiveData<UserUiState> userUiState = _userUiState;

    /**
     * Construtor para injeção de dependência via Hilt.
     *
     * @param userRepository O repositório de usuário, provido pelo Hilt.
     */
    @Inject
    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Busca os dados de um usuário específico de forma assíncrona.
     *
     * @param userId O UUID do usuário a ser buscado.
     */
    public void fetchUserById(UUID userId) {
        // Emite o estado de Loading para a UI.
        _userUiState.setValue(new UserUiState.Loading());

        // Usa o repositório para obter a chamada e a executa em background.
        userRepository.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Em caso de sucesso, emite o estado de Success com os dados do usuário.
                    _userUiState.postValue(new UserUiState.Success(response.body()));
                } else {
                    // Em caso de erro de API (ex: usuário não encontrado, 404), emite um erro.
                    _userUiState.postValue(new UserUiState.Error("Usuário não encontrado."));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Em caso de falha de rede, emite um erro.
                _userUiState.postValue(new UserUiState.Error("Falha de conexão: " + t.getMessage()));
            }
        });
    }

    /**
     * Classe para representar os estados da UI de forma explícita e segura.
     */
    public static abstract class UserUiState {
        private UserUiState() {
        }

        public static final class Loading extends UserUiState {
        }

        public static final class Success extends UserUiState {
            public final User user;

            public Success(User user) {
                this.user = user;
            }
        }

        public static final class Error extends UserUiState {
            public final String message;

            public Error(String message) {
                this.message = message;
            }
        }
    }
}
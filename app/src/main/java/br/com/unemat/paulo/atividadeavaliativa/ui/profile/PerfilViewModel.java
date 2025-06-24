package br.com.unemat.paulo.atividadeavaliativa.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import br.com.unemat.paulo.atividadeavaliativa.data.model.User;
import br.com.unemat.paulo.atividadeavaliativa.data.repository.UserRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class PerfilViewModel extends ViewModel {

    private final UserRepository userRepository;

    private final MutableLiveData<UserUiState> _userUiState = new MutableLiveData<>();
    public final LiveData<UserUiState> userUiState = _userUiState;

    @Inject
    public PerfilViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        fetchUserProfile();
    }

    public void fetchUserProfile() {
        _userUiState.setValue(new UserUiState.Loading());

        userRepository.getMyProfile().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _userUiState.postValue(new UserUiState.Success(response.body()));
                } else {
                    _userUiState.postValue(new UserUiState.Error("Falha ao carregar dados do perfil."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                _userUiState.postValue(new UserUiState.Error("Erro de conexão."));
            }
        });
    }

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
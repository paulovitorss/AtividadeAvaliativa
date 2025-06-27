package br.com.unemat.paulo.atividadeavaliativa.ui.user;

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
public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    private final MutableLiveData<UserUiState> _userUiState = new MutableLiveData<>();

    public final LiveData<UserUiState> userUiState = _userUiState;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void fetchMyProfile() {
        _userUiState.setValue(new UserUiState.Loading());

        userRepository.getMyProfile().enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _userUiState.postValue(new UserUiState.Success(response.body()));
                } else {
                    _userUiState.postValue(new UserUiState.Error("Usuário não encontrado."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                _userUiState.postValue(new UserUiState.Error("Falha de conexão: " + t.getMessage()));
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
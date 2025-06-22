package br.com.unemat.paulo.atividadeavaliativa.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginResponse;
import br.com.unemat.paulo.atividadeavaliativa.data.repository.AuthRepository;

public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;

    public LoginViewModel() {
        this.authRepository = new AuthRepository();
    }

    public LiveData<LoginResponse> login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        return authRepository.login(loginRequest);
    }
}
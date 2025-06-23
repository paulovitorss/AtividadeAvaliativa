package br.com.unemat.paulo.atividadeavaliativa.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.UUID;

import br.com.unemat.paulo.atividadeavaliativa.data.model.User;
import br.com.unemat.paulo.atividadeavaliativa.data.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repo;

    public UserViewModel(@NonNull Application app) {
        super(app);
        repo = new UserRepository(app);
    }

    public LiveData<User> getUser(UUID userId) {
        return repo.getUser(userId);
    }
}
package br.com.unemat.paulo.atividadeavaliativa.data.repository;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.unemat.paulo.atividadeavaliativa.data.model.User;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.ApiService;
import retrofit2.Call;

/**
 * Repositório responsável por buscar dados de Usuários (Users).
 * Gerenciado pelo Hilt como um Singleton para garantir uma única instância na aplicação.
 */
@Singleton
public class UserRepository {
    private final ApiService apiService;

    /**
     * Construtor para injeção de dependência via Hilt.
     * Hilt proverá a instância do ApiService que foi definida no NetworkModule.
     *
     * @param apiService A implementação da nossa interface de API.
     */
    @Inject
    public UserRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Prepara a chamada de rede para buscar um usuário específico pelo seu ID.
     * Este metodo retorna o objeto Call, permitindo que a camada que o chama (ViewModel)
     * decida como e quando executar a chamada de forma assíncrona.
     *
     * @return Um objeto Call do Retrofit, pronto para ser executado com .enqueue().
     */
    public Call<User> getMyProfile() {
        return apiService.getMyProfile();
    }
}
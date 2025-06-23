package br.com.unemat.paulo.atividadeavaliativa.data.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginResponse;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.ApiService;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import retrofit2.Call;

/**
 * Repositório responsável pelas operações de autenticação:
 * <ul>
 *   <li>delegar a chamada de login ao {@link ApiService};</li>
 *   <li>armazenar o token JWT retornado via {@link TokenManager}.</li>
 * </ul>
 * <p>
 * Escopo singleton garante que as mesmas instâncias de ApiService e TokenManager
 * sejam reutilizadas em toda a aplicação, simplificando testes e reduzindo overhead.
 * </p>
 */
@Singleton
public class AuthRepository {
    private final ApiService apiService;
    private final TokenManager tokenManager;

    /**
     * Injeta as dependências necessárias:
     * <ul>
     *   <li>{@link ApiService} gerado pelo Retrofit para chamadas de rede;</li>
     *   <li>{@link TokenManager} para persistir e recuperar o token de acesso.</li>
     * </ul>
     *
     * @param apiService   cliente Retrofit configurado em NetworkModule
     * @param tokenManager gerenciador de token via SharedPreferences/Room
     */
    @Inject
    public AuthRepository(ApiService apiService, TokenManager tokenManager) {
        this.apiService = apiService;
        this.tokenManager = tokenManager;
    }

    /**
     * Realiza a chamada de login para autenticar o usuário.
     *
     * @param loginRequest objeto contendo usuário e senha
     * @return {@link Call} de {@link LoginResponse} para enfileirar/executar
     * assincronamente ou de forma síncrona via .execute()
     */
    public Call<LoginResponse> login(LoginRequest loginRequest) {
        return apiService.login(loginRequest);
    }

    /**
     * Persiste o token JWT recebido após login bem-sucedido.
     * <p>
     * ⚠️ Atenção: usa {@code blockingAwait()} o que pode travar a thread
     * (ex.: se chamado na UI thread).
     * <strong>Melhor prática:</strong> expor um metodo assincrono ou
     * retornar um {@code Completable} para compor sem bloquear.
     * </p>
     *
     * @param token JWT a ser salvo
     */
    public void saveToken(String token) {
        try {
            tokenManager
                    .saveToken(token)
                    .blockingAwait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
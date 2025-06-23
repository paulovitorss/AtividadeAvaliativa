package br.com.unemat.paulo.atividadeavaliativa.domain.usecase;

import javax.inject.Inject;

import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginRequest;
import br.com.unemat.paulo.atividadeavaliativa.data.model.LoginResponse;
import br.com.unemat.paulo.atividadeavaliativa.data.repository.AuthRepository;
import retrofit2.Call;

/**
 * Caso de uso responsável pelo fluxo de autenticação de usuário.
 *
 * <p>
 * Encapsula a criação de {@link LoginRequest} e delega a comunicação
 * com a camada de dados via {@link AuthRepository}, promovendo
 * separação clara de responsabilidades (Clean Architecture).
 * </p>
 *
 * <p>
 * Benefícios:
 * <ul>
 *   <li><strong>Manutenibilidade:</strong> lógica de login isolada em um classe única.</li>
 *   <li><strong>Testabilidade:</strong> pode ser mockado/injetado facilmente em testes unitários.</li>
 *   <li><strong>Reuso:</strong> pode ser usado por diferentes ViewModels ou presenters.</li>
 * </ul>
 * </p>
 */
public class LoginUseCase {
    private final AuthRepository authRepository;

    /**
     * Injeção de dependência do repositório de autenticação.
     *
     * @param authRepository camada de persistência e rede para login
     */
    @Inject
    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Executa o fluxo de login, criando a requisição e disparando
     * a chamada de rede.
     *
     * @param username nome de usuário ou e-mail
     * @param password senha em texto puro (deveria vir de campo seguro)
     * @return {@link Call} de {@link LoginResponse} para enfileirar/executar
     */
    public Call<LoginResponse> invoke(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        return authRepository.login(loginRequest);
    }
}
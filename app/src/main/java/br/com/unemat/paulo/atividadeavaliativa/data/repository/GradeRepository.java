package br.com.unemat.paulo.atividadeavaliativa.data.repository;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.unemat.paulo.atividadeavaliativa.data.model.Grade;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.ApiService;
import retrofit2.Call;

/**
 * Repositório responsável por buscar dados de Notas (Grades).
 * Gerenciado pelo Hilt como um Singleton.
 */
@Singleton
public class GradeRepository {
    private final ApiService apiService;

    /**
     * Construtor para injeção de dependência via Hilt.
     * Recebe a instância do ApiService, que é provida pelo NetworkModule.
     *
     * @param apiService A implementação da nossa interface de API.
     */
    @Inject
    public GradeRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Busca a lista de notas para um estudante específico.
     * Este metodo não executa a chamada, apenas a prepara e a retorna.
     * A execução (com .enqueue()) é responsabilidade do ViewModel.
     *
     * @param studentId O UUID do estudante.
     * @return Um objeto Call do Retrofit, pronto para ser executado.
     */
    public Call<List<Grade>> getGrades(UUID studentId) {
        return apiService.getGrades(studentId);
    }
}
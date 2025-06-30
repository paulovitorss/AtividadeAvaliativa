package br.com.unemat.paulo.atividadeavaliativa.data.repository;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.unemat.paulo.atividadeavaliativa.data.model.Attendance;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.ApiService;
import retrofit2.Call;

/**
 * Repositório responsável por buscar dados de Frequência (Attendance).
 * Gerenciado pelo Hilt como um Singleton para garantir uma única instância na aplicação.
 * Sua única responsabilidade é fazer a ponte entre a fonte de dados (ApiService)
 * e a camada de lógica de negócios (ViewModel).
 */
@Singleton
public class AttendanceRepository {

    private final ApiService apiService;

    /**
     * Construtor para injeção de dependência via Hilt.
     * Hilt proverá a instância do ApiService que foi definida no seu NetworkModule.
     *
     * @param apiService A implementação da nossa interface de API.
     */
    @Inject
    public AttendanceRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Prepara a chamada de rede para buscar a lista de registros de frequência
     * para um estudante específico.
     *
     * @param studentId O ID do estudante.
     * @return Um objeto Call do Retrofit, pronto para ser executado com .enqueue() no ViewModel.
     */
    public Call<List<Attendance>> getAttendanceForStudent(UUID studentId) {
        return apiService.getAttendanceForStudent(studentId);
    }
}
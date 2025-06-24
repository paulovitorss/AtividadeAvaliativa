package br.com.unemat.paulo.atividadeavaliativa.ui.grade;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import br.com.unemat.paulo.atividadeavaliativa.data.model.Grade;
import br.com.unemat.paulo.atividadeavaliativa.data.repository.GradeRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel para a tela que exibe as notas (Grades).
 * Anotado com @HiltViewModel para ser gerenciado e injetado pelo Hilt.
 */
@HiltViewModel
public class GradeViewModel extends ViewModel {

    private final GradeRepository gradeRepository;

    // LiveData privado para o estado da UI, garantindo que só o ViewModel possa alterá-lo.
    private final MutableLiveData<GradeUiState> _gradeUiState = new MutableLiveData<>();
    // LiveData público e imutável para ser observado pela Activity/Fragment.
    public final LiveData<GradeUiState> gradeUiState = _gradeUiState;

    /**
     * Construtor para injeção de dependência via Hilt.
     *
     * @param gradeRepository O repositório de notas, provido pelo Hilt.
     */
    @Inject
    public GradeViewModel(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    /**
     * Inicia a busca assíncrona pelas notas de um estudante.
     *
     * @param studentId O UUID do estudante cujas notas serão buscadas.
     */
    public void fetchGrades(UUID studentId) {
        // Emite o estado de Loading para a UI mostrar um ProgressBar, por exemplo.
        _gradeUiState.setValue(new GradeUiState.Loading());

        // Usa o repositório para obter o objeto Call e o executa de forma assíncrona.
        gradeRepository.getGrades(studentId).enqueue(new Callback<List<Grade>>() {
            @Override
            public void onResponse(Call<List<Grade>> call, Response<List<Grade>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Em caso de sucesso, emite o estado Success com a lista de notas.
                    _gradeUiState.postValue(new GradeUiState.Success(response.body()));
                } else {
                    // Em caso de erro de API (404, 500, etc.), emite um estado de erro.
                    _gradeUiState.postValue(new GradeUiState.Error("Falha ao carregar as notas."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Grade>> call, @NonNull Throwable t) {
                // Em caso de falha de rede, emite um estado de erro com a mensagem.
                _gradeUiState.postValue(new GradeUiState.Error("Erro de conexão: " + t.getMessage()));
            }
        });
    }

    /**
     * Classe selada (simulada em Java com construtor privado) para representar os
     * diferentes estados da UI de forma segura e explícita.
     */
    public static abstract class GradeUiState {
        private GradeUiState() {
        }

        public static final class Loading extends GradeUiState {
        }

        public static final class Success extends GradeUiState {
            public final List<Grade> grades;

            public Success(List<Grade> grades) {
                this.grades = grades;
            }
        }

        public static final class Error extends GradeUiState {
            public final String message;

            public Error(String message) {
                this.message = message;
            }
        }
    }
}
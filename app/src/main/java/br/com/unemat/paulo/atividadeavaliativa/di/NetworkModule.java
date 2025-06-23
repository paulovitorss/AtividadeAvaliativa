package br.com.unemat.paulo.atividadeavaliativa.di;

import android.content.Context;

import javax.inject.Singleton;

import br.com.unemat.paulo.atividadeavaliativa.data.remote.ApiService;
import br.com.unemat.paulo.atividadeavaliativa.data.remote.AuthInterceptor;
import br.com.unemat.paulo.atividadeavaliativa.security.TokenManager;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Módulo de DI responsável por fornecer instâncias de rede:
 * <ul>
 *   <li>{@link TokenManager}: gerencia tokens de autenticação.</li>
 *   <li>{@link AuthInterceptor}: adiciona header de auth em cada requisição.</li>
 *   <li>{@link OkHttpClient}: cliente HTTP configurado com interceptors.</li>
 *   <li>{@link Retrofit}: adaptador REST/JSON.</li>
 *   <li>{@link ApiService}: interface de API para chamadas Retrofit.</li>
 * </ul>
 *
 * <p>
 * Escopo único (singleton) garante reuso de instâncias, reduz overhead
 * e evita inconsistências de estado em múltiplas requisições.
 * </p>
 */
@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    /**
     * URL base de todas as chamadas de API.
     * <p>
     * Atenção: manter a barra final (“/”) para que Retrofit combine corretamente
     * caminhos relativos declarados em {@link ApiService}.
     * </p>
     */
    private static final String BASE_URL = "http://172.32.236.240:8080/";

    /**
     * Fornece um {@link TokenManager} único para toda a aplicação.
     *
     * @param context Contexto de aplicação para persistência segura de tokens.
     * @return instância singleton de TokenManager.
     */
    @Provides
    @Singleton
    public TokenManager provideTokenManager(@ApplicationContext Context context) {
        return TokenManager.getInstance(context);
    }

    /**
     * Cria o {@link AuthInterceptor}, responsável por:
     * <ul>
     *   <li>Recuperar token via {@link TokenManager}.</li>
     *   <li>Injetar header “Authorization: Bearer &lt;token&gt;” em cada request.</li>
     * </ul>
     *
     * @param tokenManager gerenciador de tokens injetado.
     * @return nova instância de AuthInterceptor.
     */
    @Provides
    @Singleton
    public AuthInterceptor provideAuthInterceptor(TokenManager tokenManager) {
        return new AuthInterceptor(tokenManager);
    }

    /**
     * Configura o {@link OkHttpClient} com:
     * <ul>
     *   <li>{@link AuthInterceptor} para autenticação automática.</li>
     *   <li>{@link HttpLoggingInterceptor} em nível BODY (útil em DEBUG).</li>
     * </ul>
     *
     * <p>
     * Boa prática: em produção, condicionar o logging a BuildConfig.DEBUG
     * para não vazar dados sensíveis.
     * </p>
     *
     * @param authInterceptor interceptor que injeta token nas requisições.
     * @return cliente HTTP configurado.
     */
    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(AuthInterceptor authInterceptor) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    /**
     * Constrói o adaptador {@link Retrofit} para chamadas REST/JSON.
     *
     * @param okHttpClient cliente HTTP já configurado com interceptors.
     * @return instância singleton de Retrofit.
     */
    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Gera a implementação de {@link ApiService} a partir do Retrofit.
     *
     * @param retrofit adaptador REST configurado.
     * @return proxy da interface ApiService para chamadas de endpoint.
     */
    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
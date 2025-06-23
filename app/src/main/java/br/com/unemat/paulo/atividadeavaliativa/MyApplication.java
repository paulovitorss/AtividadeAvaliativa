package br.com.unemat.paulo.atividadeavaliativa;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Ponto de entrada principal para a aplicação.
 * A anotação @HiltAndroidApp ativa a geração de código do Hilt e cria o
 * contêiner de dependências que viverá no ciclo de vida do aplicativo.
 * Esta classe é essencial para que o Hilt funcione.
 */
@HiltAndroidApp
public class MyApplication extends Application {
}

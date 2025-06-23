package br.com.unemat.paulo.atividadeavaliativa.security;

import android.content.Context;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class TokenManager {
    private static volatile TokenManager INSTANCE;

    private static final String DATASTORE_FILE_NAME = "jwt_datastore";
    private static final Preferences.Key<String> TOKEN_KEY = PreferencesKeys.stringKey("auth_token");
    private final RxDataStore<Preferences> dataStore;

    private TokenManager(Context context) {
        this.dataStore = new RxPreferenceDataStoreBuilder(context.getApplicationContext(), DATASTORE_FILE_NAME).build();
    }

    public static TokenManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TokenManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TokenManager(context);
                }
            }
        }
        return INSTANCE;
    }

    public Completable saveToken(String token) {
        return dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            if (token == null || token.trim().isEmpty()) {
                mutablePreferences.remove(TOKEN_KEY);
            } else {
                mutablePreferences.set(TOKEN_KEY, token);
            }
            return Single.just(mutablePreferences);
        }).ignoreElement();
    }

    public Single<String> getToken() {
        return dataStore.data()
                .map(prefs -> {
                    String token = prefs.get(TOKEN_KEY);
                    return token == null ? "" : token;
                })
                .first("");
    }

    public Completable clearToken() {
        return saveToken(null);
    }

    public String getTokenSync() {
        try {
            return getToken().blockingGet();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
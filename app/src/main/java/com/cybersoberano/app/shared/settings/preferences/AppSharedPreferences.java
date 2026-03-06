/* * Créditos: dev Leandro - CyberSoberano
 * Gerenciador de Preferências do App.
 * Armazena configurações persistentes de forma segura e sincronizada entre processos.
 */

package com.cybersoberano.app.shared.settings.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppSharedPreferences {

    protected final Context mContext;

    // Preferências padrão do app
    protected final SharedPreferences mSharedPreferences;

    // Preferências acessíveis por múltiplos processos (Terminal + Bots em background)
    protected final SharedPreferences mMultiProcessSharedPreferences;

    protected AppSharedPreferences(@NonNull Context context,
                                   @Nullable SharedPreferences sharedPreferences,
                                   @Nullable SharedPreferences multiProcessSharedPreferences) {
        mContext = context;
        mSharedPreferences = sharedPreferences;
        mMultiProcessSharedPreferences = multiProcessSharedPreferences;
    }

    public Context getContext() { return mContext; }
    public SharedPreferences getSharedPreferences() { return mSharedPreferences; }
    public SharedPreferences getMultiProcessSharedPreferences() { return mMultiProcessSharedPreferences; }
}
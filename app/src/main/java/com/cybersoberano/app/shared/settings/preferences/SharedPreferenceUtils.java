/* * Créditos: dev Leandro - CyberSoberano
 * Utilitários de Persistência de Dados.
 * Facilita a leitura e escrita de configurações, garantindo estabilidade.
 */

package com.cybersoberano.app.shared.settings.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import com.cybersoberano.app.shared.logger.Logger;

public class SharedPreferenceUtils {

    private static final String LOG_TAG = "SharedPreferenceUtils";

    /** Cria/Acessa o arquivo de preferências em modo privado */
    public static SharedPreferences getPrivateSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /** Acessa preferências permitindo que múltiplos processos leiam/escrevam ao mesmo tempo */
    public static SharedPreferences getPrivateAndMultiProcessSharedPreferences(Context context, String name) {
        // 0x0004 é o valor da constante MODE_MULTI_PROCESS
        return context.getSharedPreferences(name, Context.MODE_PRIVATE | 0x0004);
    }

    /** --- Métodos para BOOLEAN --- */
    public static boolean getBoolean(SharedPreferences sp, String key, boolean def) {
        if (sp == null) return def;
        try {
            return sp.getBoolean(key, def);
        } catch (Exception e) {
            Logger.logError(LOG_TAG, "Erro ao ler chave boolean: " + key);
            return def;
        }
    }

    @SuppressLint("ApplySharedPref")
    public static void setBoolean(SharedPreferences sp, String key, boolean value, boolean commitToFile) {
        if (sp == null) return;
        SharedPreferences.Editor editor = sp.edit().putBoolean(key, value);
        if (commitToFile) editor.commit(); else editor.apply();
    }

    /** --- Métodos para INT --- */
    public static int getInt(SharedPreferences sp, String key, int def) {
        if (sp == null) return def;
        try {
            return sp.getInt(key, def);
        } catch (Exception e) {
            Logger.logError(LOG_TAG, "Erro ao ler chave int: " + key);
            return def;
        }
    }

    @SuppressLint("ApplySharedPref")
    public static void setInt(SharedPreferences sp, String key, int value, boolean commitToFile) {
        if (sp == null) return;
        SharedPreferences.Editor editor = sp.edit().putInt(key, value);
        if (commitToFile) editor.commit(); else editor.apply();
    }

    /** --- Métodos para STRING --- */
    public static String getString(SharedPreferences sp, String key, @Nullable String def) {
        return getString(sp, key, def, false);
    }

    public static String getString(SharedPreferences sp, String key, @Nullable String def, boolean trim) {
        if (sp == null) return def;
        try {
            String value = sp.getString(key, def);
            if (value != null && trim) value = value.trim();
            return value;
        } catch (Exception e) {
            Logger.logError(LOG_TAG, "Erro ao ler chave String: " + key);
            return def;
        }
    }

    @SuppressLint("ApplySharedPref")
    public static void setString(SharedPreferences sp, String key, String value, boolean commitToFile) {
        if (sp == null) return;
        SharedPreferences.Editor editor = sp.edit().putString(key, value);
        if (commitToFile) editor.commit(); else editor.apply();
    }

    /** --- Métodos para LONG --- */
    public static long getLong(SharedPreferences sp, String key, long def) {
        if (sp == null) return def;
        try {
            return sp.getLong(key, def);
        } catch (Exception e) {
            Logger.logError(LOG_TAG, "Erro ao ler chave long: " + key);
            return def;
        }
    }

    @SuppressLint("ApplySharedPref")
    public static void setLong(SharedPreferences sp, String key, long value, boolean commitToFile) {
        if (sp == null) return;
        SharedPreferences.Editor editor = sp.edit().putLong(key, value);
        if (commitToFile) editor.commit(); else editor.apply();
    }
}

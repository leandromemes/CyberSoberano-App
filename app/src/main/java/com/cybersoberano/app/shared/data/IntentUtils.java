/* * Créditos: dev Leandro - CyberSoberano
 * Utilitário para extração segura de dados de Intents e Bundles.
 * Essencial para a comunicação entre o terminal e o serviço do bot.
 */

package com.cybersoberano.app.shared.data;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import java.util.Arrays;

public class IntentUtils {

    private static final String LOG_TAG = "IntentUtils";

    /**
     * Obtém uma String de uma Intent apenas se ela existir e não estiver vazia.
     */
    public static String getStringExtraIfSet(@NonNull Intent intent, String key, String def) {
        String value = intent.getStringExtra(key);
        if (value == null || value.isEmpty()) {
            return (def != null && !def.isEmpty()) ? def : null;
        }
        return value;
    }

    /**
     * Obtém um Inteiro armazenado como String em uma Intent.
     * Útil para capturar portas de conexão ou PIDs vindos do WhatsApp.
     */
    public static Integer getIntegerExtraIfSet(@NonNull Intent intent, String key, Integer def) {
        try {
            String value = intent.getStringExtra(key);
            if (value == null || value.isEmpty()) return def;
            return Integer.parseInt(value);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Transforma todo o conteúdo de uma Intent em uma String legível (Markdown).
     * Perfeito para debugar o que o bot está recebendo.
     */
    public static String getIntentString(Intent intent) {
        if (intent == null) return null;
        return intent.toString() + "\n" + getBundleString(intent.getExtras());
    }

    /**
     * Analisa o 'pacote' (Bundle) e extrai todos os pares chave-valor.
     */
    public static String getBundleString(Bundle bundle) {
        if (bundle == null || bundle.size() == 0) return "Bundle[]";

        StringBuilder bundleString = new StringBuilder("Bundle[\n");
        boolean first = true;
        for (String key : bundle.keySet()) {
            if (!first) bundleString.append("\n");
            bundleString.append(key).append(": `");

            Object value = bundle.get(key);
            // Verifica o tipo do dado para converter corretamente para texto
            if (value instanceof int[]) {
                bundleString.append(Arrays.toString((int[]) value));
            } else if (value instanceof String[]) {
                bundleString.append(Arrays.toString((String[]) value));
            } else if (value instanceof Bundle) {
                bundleString.append(getBundleString((Bundle) value));
            } else {
                bundleString.append(value);
            }

            bundleString.append("` ");
            first = false;
        }
        bundleString.append("\n]");
        return bundleString.toString();
    }
}
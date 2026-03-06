/* * Créditos: dev Leandro - CyberSoberano
 * Interface de Processamento de Propriedades.
 * Define como o texto bruto dos arquivos vira lógica de programação.
 */

package com.cybersoberano.app.shared.settings.properties;

import android.content.Context;
import androidx.annotation.NonNull;
import java.util.Properties;

public interface SharedPropertiesParser {

    /** * Intercepta os dados brutos do disco.
     * Útil para validar o arquivo inteiro antes do app usar.
     */
    @NonNull
    Properties preProcessPropertiesOnReadFromDisk(@NonNull Context context, @NonNull Properties properties);

    /** * Transforma o valor de texto (String) no objeto Java correto.
     * Ex: String "123" -> Integer 123.
     */
    Object getInternalPropertyValueFromValue(@NonNull Context context, String key, String value);
}
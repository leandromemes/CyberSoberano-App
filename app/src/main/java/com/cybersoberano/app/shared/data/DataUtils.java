/* * Créditos: dev Leandro - CyberSoberano
 * Utilitário para manipulação, conversão e limpeza de dados.
 * Garante que o terminal processe strings e números sem causar falhas de transação.
 */

package com.cybersoberano.app.shared.data;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.base.Strings;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DataUtils {

    // Limite de 100KB para evitar que o Android mate o processo em transferências de dados
    public static final int TRANSACTION_SIZE_LIMIT_IN_BYTES = 100 * 1024;
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Trunca a saída de comandos do bot para não estourar o limite de caracteres.
     */
    public static String getTruncatedCommandOutput(String text, int maxLength, boolean fromEnd, boolean onNewline, boolean addPrefix) {
        if (text == null) return null;
        String prefix = "(truncado) ";

        if (addPrefix) maxLength = maxLength - prefix.length();
        if (maxLength < 0 || text.length() < maxLength) return text;

        if (fromEnd) {
            text = text.substring(0, maxLength);
        } else {
            int cutOffIndex = text.length() - maxLength;
            if (onNewline) {
                int nextNewlineIndex = text.indexOf('\n', cutOffIndex);
                if (nextNewlineIndex != -1 && nextNewlineIndex != text.length() - 1) {
                    cutOffIndex = nextNewlineIndex + 1;
                }
            }
            text = text.substring(cutOffIndex);
        }

        return addPrefix ? prefix + text : text;
    }

    /** Converte bytes para Hexadecimal - Útil para Cybersecurity e análise de pacotes. */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /** Converte String para Inteiro sem risco de Crash. */
    public static int getIntFromString(String value, int def) {
        if (value == null) return def;
        try { return Integer.parseInt(value); }
        catch (Exception e) { return def; }
    }

    /** Adiciona indentação de 4 espaços - Deixa o código/log elegante no WhatsApp. */
    public static String getSpaceIndentedString(String string, int count) {
        if (string == null || string.isEmpty()) return string;
        return string.replaceAll("(?m)^", Strings.repeat("    ", Math.max(count, 1)));
    }

    /** Verifica se a string é nula ou vazia. */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
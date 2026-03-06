package com.cybersoberano.app.shared.logger;

import android.util.Log;

public class Logger {

    private static final String DEFAULT_TAG = "CyberSoberano";

    // Adicionado para corrigir o erro: symbol: method logInfo(String)
    public static void logInfo(String message) {
        Log.i(DEFAULT_TAG, message);
    }

    // Adicionado para corrigir o erro: symbol: method logInfo(String, String)
    public static void logInfo(String tag, String message) {
        Log.i(tag, message);
    }

    // Corrigido para aceitar apenas a mensagem (erro reportado em FileTypes.java)
    public static void logError(String message) {
        Log.e(DEFAULT_TAG, message);
    }

    // Mantido para as chamadas que usam TAG e Mensagem
    public static void logError(String tag, String message) {
        Log.e(tag, message);
    }

    // Adicionado para corrigir os erros de StackTrace com Exception
    public static void logStackTraceWithMessage(String tag, String message, Exception e) {
        Log.e(tag, message + " | Error: " + e.getMessage());
        e.printStackTrace();
    }
}

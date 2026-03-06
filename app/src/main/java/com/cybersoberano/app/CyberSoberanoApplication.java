package com.cybersoberano.app;

import android.app.Application;

/**
 * Classe de Aplicação do CyberSoberano.
 * Removido o import do TermuxShellManager que não existe mais.
 */
public class CyberSoberanoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializações globais podem vir aqui
    }
}

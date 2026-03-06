package com.cybersoberano.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Serviço que mantém o terminal rodando em segundo plano.
 * Créditos: dev Leandro - CyberSoberano
 */
public final class TerminalService extends Service {

    private final IBinder mBinder = new TerminalServiceBinder();

    public final class TerminalServiceBinder extends Binder {
        public TerminalService getService() {
            return TerminalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    // Créditos: dev Leandro - CyberSoberano
}
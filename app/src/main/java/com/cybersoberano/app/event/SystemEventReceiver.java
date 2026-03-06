// Créditos: dev Leandro - CyberSoberano
package com.cybersoberano.app.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.cybersoberano.app.TermuxService;

public class SystemEventReceiver extends BroadcastReceiver {
    private static final String TAG = "CyberSoberano_Event";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) return;

        String action = intent.getAction();
        Log.d(TAG, "Evento do sistema recebido: " + action);

        // Se o celular ligar, inicia o serviço do bot automaticamente
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Intent serviceIntent = new Intent(context, TermuxService.class);
            // No Android 8+, serviços em background pós-boot precisam ser iniciados assim:
            context.startForegroundService(serviceIntent);
        }
    }
}
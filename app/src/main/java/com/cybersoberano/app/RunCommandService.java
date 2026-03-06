package com.cybersoberano.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Service para execução de comandos.
 * Limpo para evitar erros de compilação.
 */
public class RunCommandService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

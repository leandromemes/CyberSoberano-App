package com.cybersoberano.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TermuxService extends Service {

    private static final String TAG = "TermuxService";
    private static final String CHANNEL_ID = "CyberSoberanoChannel";
    private static boolean isNodeRunning = false;
    private static boolean libsLoaded = false;

    static {
        try {
            System.loadLibrary("node");
            System.loadLibrary("cybersoberano-term");
            libsLoaded = true;
            Log.d(TAG, "Bibliotecas nativas carregadas com sucesso.");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "ERRO: Arquitetura incompatível ou lib ausente: " + e.getMessage());
        }
    }

    public native int startNode(String[] args);

    @Override
    public void onCreate() {
        super.onCreate();
        criarCanalNotificacao();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("CyberSoberano Bot")
            .setContentText("Motor em execução...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true);

        Notification notification = builder.build();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
            } else {
                startForeground(1, notification);
            }
        } catch (Exception e) {
            startForeground(1, notification);
        }

        if (!isNodeRunning) {
            iniciarMotorNode();
        }

        return START_STICKY;
    }

    private void iniciarMotorNode() {
        new Thread(() -> {
            if (!libsLoaded) {
                enviarBroadcastParaTerminal("❌ ERRO: Bibliotecas nativas não carregadas!");
                enviarBroadcastParaTerminal("Verifique se o Emulador é compatível com x86_64.");
                pararServico();
                return;
            }

            isNodeRunning = true;
            try {
                File botFolder = new File(getFilesDir(), "bot");
                enviarBroadcastParaTerminal(">> Preparando ambiente...");

                // Extrai os arquivos para a memória interna
                copyAssetsToInternalStorage("bot", botFolder.getAbsolutePath());

                File indexJs = new File(botFolder, "index.js");
                if (!indexJs.exists()) {
                    enviarBroadcastParaTerminal("❌ index.js não encontrado nos assets!");
                    pararServico();
                    return;
                }

                enviarBroadcastParaTerminal(">> Iniciando motor Node.js...");

                List<String> argsList = new ArrayList<>();
                argsList.add("node");
                argsList.add(indexJs.getAbsolutePath());

                // Execução Nativa
                int status = startNode(argsList.toArray(new String[0]));

                enviarBroadcastParaTerminal(">> Bot encerrado. Status: " + status);

            } catch (Exception e) {
                enviarBroadcastParaTerminal("❌ Falha crítica: " + e.getMessage());
                Log.e(TAG, "Erro na execução", e);
            } finally {
                isNodeRunning = false;
                pararServico();
            }
        }).start();
    }

    private void copyAssetsToInternalStorage(String assetPath, String localPath) throws IOException {
        AssetManager assetManager = getAssets();
        String[] assets = assetManager.list(assetPath);
        File targetDir = new File(localPath);
        if (!targetDir.exists()) targetDir.mkdirs();

        if (assets == null || assets.length == 0) {
            copyFile(assetPath, localPath);
        } else {
            for (String asset : assets) {
                copyAssetsToInternalStorage(assetPath + "/" + asset, localPath + "/" + asset);
            }
        }
    }

    private void copyFile(String assetPath, String localPath) throws IOException {
        try (InputStream in = getAssets().open(assetPath);
             OutputStream out = new FileOutputStream(localPath)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
    }

    private void pararServico() {
        stopForeground(true);
        stopSelf();
    }

    private void criarCanalNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID, "Serviço Bot", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(serviceChannel);
        }
    }

    private void enviarBroadcastParaTerminal(String mensagem) {
        Intent intent = new Intent("com.cybersoberano.app.TERMINAL_UPDATE");
        intent.putExtra("mensagem", mensagem);
        sendBroadcast(intent);
        Log.d(TAG, mensagem);
    }

    @Override
    public void onDestroy() {
        isNodeRunning = false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }
}

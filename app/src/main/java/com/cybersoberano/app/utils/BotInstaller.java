package com.cybersoberano.app.utils;

import android.content.Context;
import android.util.Log;
import java.io.*;
import java.util.zip.*;

/**
 * Instalador robusto para o motor Gótica Bot.
 * Créditos: dev Leandro - CyberSoberano
 */
public class BotInstaller {

    private static final String TAG = "BotInstaller";

    public static void install(Context context, String zipName) throws Exception {
        // Define a pasta de destino como 'gotica-bot' dentro dos arquivos do app
        File outDir = new File(context.getFilesDir(), "gotica-bot");

        // Se a pasta já existe, podemos limpar para uma instalação limpa (opcional)
        // deleteRecursive(outDir);

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        // Busca o zip em assets/bin/bot.zip conforme solicitado
        InputStream is = context.getAssets().open("bin/" + zipName);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
        ZipEntry ze;
        byte[] buffer = new byte[8192];

        try {
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(outDir, ze.getName());

                if (ze.isDirectory()) {
                    file.mkdirs();
                } else {
                    // Garante que as subpastas existam (ex: node_modules, src)
                    File parent = file.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }

                    // Extração do arquivo
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        int count;
                        while ((count = zis.read(buffer)) != -1) {
                            fos.write(buffer, 0, count);
                        }
                    }
                }
                zis.closeEntry();
            }
        } finally {
            zis.close();
            is.close();
        }
        Log.d(TAG, "Extração concluída com sucesso em: " + outDir.getAbsolutePath());
    }

    // Função utilitária para limpar a pasta antes de atualizar o bot
    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}
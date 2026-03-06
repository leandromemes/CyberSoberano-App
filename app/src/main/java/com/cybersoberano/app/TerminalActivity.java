package com.cybersoberano.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/* * Desenvolvido por Leandro | CyberSoberano
 * Terminal com Barra de Progresso e Extração Local [cite: 2026-01-28, 2026-03-02]
 */
public class TerminalActivity extends AppCompatActivity {

    private TextView txtLog;
    private ScrollView scroll;
    private Button btnInstalar, btnEnviar, btnParar;
    private EditText editComando;
    private ProgressBar progress;
    private String appFilesPath;
    private PrintWriter writer;
    private Process currentProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);

        txtLog = findViewById(R.id.txtLogTerminal);
        scroll = findViewById(R.id.scrollTerminal);
        btnInstalar = findViewById(R.id.btnInstalarBot);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnParar = findViewById(R.id.btnParar);
        editComando = findViewById(R.id.editComando);
        progress = findViewById(R.id.progressInstalacao);

        appFilesPath = getFilesDir().getAbsolutePath();

        btnInstalar.setOnClickListener(v -> {
            btnInstalar.setEnabled(false);
            progress.setVisibility(View.VISIBLE); // Mostra progresso [cite: 2026-03-02]
            iniciarProcessoHacker();
        });

        btnParar.setOnClickListener(v -> {
            if (currentProcess != null) {
                currentProcess.destroy();
                logTerminal("\n🛑 [SISTEMA]: Bot interrompido.");
                btnInstalar.setEnabled(true);
                progress.setVisibility(View.GONE);
            }
        });

        btnEnviar.setOnClickListener(v -> {
            String cmd = editComando.getText().toString();
            if (writer != null && !cmd.isEmpty()) {
                writer.println(cmd);
                writer.flush();
                editComando.setText("");
            }
        });
    }

    private void iniciarProcessoHacker() {
        new Thread(() -> {
            logTerminal("--- INICIANDO SETUP CYBERSOBERANO ---");
            instalarMotorNode();
            extrairBotLocal();

            String pastaBot = appFilesPath + "/gotica-bot";

            logTerminal("> Baixando Módulos (Isso pode demorar alguns minutos)...");
            executarNoShell("cd " + pastaBot + " && yarn install");

            // Esconde o progresso quando terminar a instalação pesada [cite: 2026-03-02]
            new Handler(Looper.getMainLooper()).post(() -> progress.setVisibility(View.GONE));

            logTerminal("> Iniciando Bot Gotica... [cite: 2026-02-13]");
            executarNoShell("cd " + pastaBot + " && npm start");
        }).start();
    }

    private void extrairBotLocal() {
        try {
            logTerminal("> Extraindo bot.zip...");
            File pastaBot = new File(appFilesPath + "/gotica-bot");
            if (!pastaBot.exists()) pastaBot.mkdirs();

            InputStream is = getAssets().open("bot.zip");
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry ze;

            while ((ze = zis.getNextEntry()) != null) {
                File f = new File(pastaBot, ze.getName());
                if (ze.isDirectory()) {
                    f.mkdirs();
                } else {
                    f.getParentFile().mkdirs();
                    FileOutputStream fos = new FileOutputStream(f);
                    byte[] buffer = new byte[1024];
                    int count;
                    while ((count = zis.read(buffer)) != -1) fos.write(buffer, 0, count);
                    fos.close();
                }
                zis.closeEntry();
            }
            zis.close();
        } catch (Exception e) {
            logTerminal("❌ Erro extração: " + e.getMessage());
        }
    }

    private void instalarMotorNode() {
        try {
            File pastaBin = new File(getFilesDir(), "bin");
            if (!pastaBin.exists()) pastaBin.mkdirs();
            File arquivoNode = new File(pastaBin, "node");

            if (!arquivoNode.exists()) {
                logTerminal("> Instalando binários...");
                InputStream in = getAssets().open("bin/node");
                OutputStream out = new FileOutputStream(arquivoNode);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
                in.close();
                out.close();
                Runtime.getRuntime().exec("chmod 755 " + arquivoNode.getAbsolutePath());
            }
        } catch (Exception e) {
            logTerminal("❌ Erro binários: " + e.getMessage());
        }
    }

    private void executarNoShell(String comando) {
        try {
            ProcessBuilder pb = new ProcessBuilder("/system/bin/sh", "-c", comando);
            Map<String, String> env = pb.environment();
            env.put("PATH", env.get("PATH") + ":" + appFilesPath + "/bin");
            env.put("HOME", appFilesPath);

            currentProcess = pb.start();
            writer = new PrintWriter(currentProcess.getOutputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(currentProcess.getErrorStream()));

            String linha;
            while ((linha = reader.readLine()) != null) logTerminal(linha);
            while ((linha = errorReader.readLine()) != null) logTerminal("⚠️ [LOG]: " + linha);

            currentProcess.waitFor();
        } catch (Exception e) {
            logTerminal("❌ Erro shell: " + e.getMessage());
        }
    }

    private void logTerminal(String mensagem) {
        new Handler(Looper.getMainLooper()).post(() -> {
            txtLog.append("\n" + mensagem);
            scroll.post(() -> scroll.fullScroll(ScrollView.FOCUS_DOWN));
        });
    }
}
// Créditos: dev Leandro - CyberSoberano [cite: 2026-01-31]
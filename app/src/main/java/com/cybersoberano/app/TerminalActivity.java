package com.cybersoberano.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
<<<<<<< HEAD
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class TerminalActivity extends AppCompatActivity {

    private TextView txtCodigoPareamento, txtStatusTerminal;
    private EditText editNumeroBot;
    private Button btnGerarCodigo;
    private DatabaseReference mDatabase;
    private ValueEventListener listenerAtual;
    private String numeroMonitorado = "";
=======
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
>>>>>>> origin/master

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);

<<<<<<< HEAD
        try {
            // Importante: Verifique se a URL do Firebase no google-services.json está correta
            mDatabase = FirebaseDatabase.getInstance().getReference("conexoes_whatsapp");
        } catch (Exception e) {
            Toast.makeText(this, "Erro Firebase!", Toast.LENGTH_LONG).show();
        }

        txtCodigoPareamento = findViewById(R.id.txtCodigoPareamento);
        txtStatusTerminal = findViewById(R.id.txtStatusTerminal);
        editNumeroBot = findViewById(R.id.editNumeroBot);
        btnGerarCodigo = findViewById(R.id.btnGerarCodigo);

        btnGerarCodigo.setOnClickListener(v -> solicitarConexao());
    }

    private void solicitarConexao() {
        String numeroRaw = editNumeroBot.getText().toString().trim();
        String numeroLimpo = numeroRaw.replaceAll("\\D", "");

        if (numeroLimpo.length() < 10) {
            Toast.makeText(this, "Digite DDD + Número!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!numeroLimpo.startsWith("55")) {
            numeroLimpo = "55" + numeroLimpo;
        }

        final String numeroFinal = numeroLimpo;
        this.numeroMonitorado = numeroFinal;

        btnGerarCodigo.setEnabled(false);
        btnGerarCodigo.setText("GERANDO...");

        txtStatusTerminal.setText(">> [SISTEMA] Solicitando código...");
        txtCodigoPareamento.setText("...");

        // USAR UPDATE EM VEZ DE SETVALUE PARA NÃO APAGAR O NÓ INTEIRO
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "solicitando");
        updates.put("requestPairing", true);
        updates.put("pairingCode", "gerando...");
        updates.put("updatedAt", System.currentTimeMillis());

        mDatabase.child(numeroFinal).updateChildren(updates).addOnSuccessListener(aVoid -> {
            txtStatusTerminal.append("\n>> [FIREBASE] Aguardando Bot...");
            ouvirResposta(numeroFinal);
        }).addOnFailureListener(e -> {
            txtStatusTerminal.append("\n>> [ERRO]: " + e.getMessage());
            btnGerarCodigo.setEnabled(true);
        });
    }

    private void ouvirResposta(String numero) {
        if (listenerAtual != null) {
            mDatabase.child(numero).removeEventListener(listenerAtual);
        }

        listenerAtual = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;

                // Pegamos os valores brutos para evitar erro de cast
                Object statusObj = snapshot.child("status").getValue();
                Object codigoObj = snapshot.child("pairingCode").getValue();

                String status = statusObj != null ? statusObj.toString() : "";
                String codigo = codigoObj != null ? codigoObj.toString() : "";

                // LOG DE DEBUG NO TERMINAL DO APP
                if (!codigo.isEmpty() && !codigo.equals("gerando...")) {
                    // Força a atualização na UI Thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        txtCodigoPareamento.setText(codigo);
                        if (!txtStatusTerminal.getText().toString().contains(codigo)) {
                            txtStatusTerminal.append("\n>> [BOT] CÓDIGO RECEBIDO!");
                        }
                    });
                }

                if ("conectado".equals(status)) {
                    txtCodigoPareamento.setText("OK");
                    txtStatusTerminal.append("\n>> [!] BOT ONLINE!");
                    btnGerarCodigo.setEnabled(true);
                    btnGerarCodigo.setText("CONECTADO");
                }

                if ("aguardando_codigo".equals(status)) {
                    txtStatusTerminal.append("\n>> [!] Código pronto no WhatsApp.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        mDatabase.child(numero).addValueEventListener(listenerAtual);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerAtual != null && !numeroMonitorado.isEmpty()) {
            mDatabase.child(numeroMonitorado).removeEventListener(listenerAtual);
        }
    }
}
=======
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
>>>>>>> origin/master

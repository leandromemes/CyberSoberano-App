package com.cybersoberano.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.net.InetAddress;

public class DnsActivity extends AppCompatActivity {
    private TextView txtResultado;
    private EditText editAlvo;
    private String logFinal = "";
    private int charIndex = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dns);

        txtResultado = findViewById(R.id.txtResultadoDns);
        editAlvo = findViewById(R.id.editAlvo);
        Button btnAnalisar = findViewById(R.id.btnAnalisar);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        btnAnalisar.setOnClickListener(v -> {
            String alvo = editAlvo.getText().toString().trim();
            if (!alvo.isEmpty()) iniciarAnalise(alvo);
        });

        findViewById(R.id.btnCopiar).setOnClickListener(v -> {
            if (!logFinal.isEmpty()) {
                ClipboardManager cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cb.setPrimaryClip(ClipData.newPlainText("DnsLog", logFinal));
                Toast.makeText(this, "Log copiado!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarAnalise(String host) {
        txtResultado.setText(">> INICIANDO RESOLUÇÃO DNS...\n");
        new Thread(() -> {
            try {
                InetAddress address = InetAddress.getByName(host);
                logFinal = "\n>> [RESULTADOS DNS]\n\n" +
                        "🎯 ALVO: " + host + "\n" +
                        "🌐 IP RESOLVIDO: " + address.getHostAddress() + "\n" +
                        "🏷️ CANONICAL: " + address.getCanonicalHostName() + "\n" +
                        "📡 STATUS: ATIVO / ONLINE\n\n" +
                        ">> ANÁLISE CONCLUÍDA.";

                runOnUiThread(() -> {
                    txtResultado.setText("");
                    charIndex = 0;
                    handler.post(typewriter);
                });
            } catch (Exception e) {
                runOnUiThread(() -> txtResultado.setText(">> ERRO: ALVO NÃO ENCONTRADO."));
            }
        }).start();
    }

    private final Runnable typewriter = new Runnable() {
        @Override
        public void run() {
            if (charIndex < logFinal.length()) {
                txtResultado.append(String.valueOf(logFinal.charAt(charIndex)));
                charIndex++;
                handler.postDelayed(this, 30);
            }
        }
    };
}
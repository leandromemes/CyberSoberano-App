package com.cybersoberano.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/* * Desenvolvido por Leandro | CyberSoberano
 * Rastreador de IP com Efeito Terminal de Elite
 */
public class RastreadorIpActivity extends AppCompatActivity {
    private TextView txtResultado;
    private ProgressBar progressBar;
    private String relatorioFinal = "";
    private int charIndex = 0;
    private final Handler typewriterHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rastreador);

        txtResultado = findViewById(R.id.txtResultadoFinal);
        progressBar = findViewById(R.id.progressoRastreio);
        Button btnCopiar = findViewById(R.id.btnCopiar);
        View btnVoltar = findViewById(R.id.btnVoltar);

        if (btnVoltar != null) btnVoltar.setOnClickListener(v -> finish());

        if (btnCopiar != null) {
            btnCopiar.setOnClickListener(v -> {
                if (!relatorioFinal.isEmpty() && charIndex >= relatorioFinal.length()) {
                    ClipboardManager cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cb.setPrimaryClip(ClipData.newPlainText("GeoIP", relatorioFinal));
                    Toast.makeText(this, "Relatório copiado!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        executarBuscaGeoHacker();
    }

    private void executarBuscaGeoHacker() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        txtResultado.setText(">> ESTABELECENDO CONEXÃO SATELLITAL...\n");

        new Thread(() -> {
            try {
                URL url = new URL("http://ip-api.com/json/?fields=status,message,country,countryCode,regionName,city,zip,lat,lon,timezone,isp,org,as,query");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);

                JSONObject json = new JSONObject(sb.toString());
                if (json.getString("status").equals("success")) {
                    relatorioFinal = "\n>> [DADOS RECUPERADOS]\n\n" +
                            "🌐 IP PÚBLICO: " + json.getString("query") + "\n" +
                            "🏢 PROVEDOR: " + json.getString("isp") + "\n" +
                            "🛡️ ORGANIZAÇÃO: " + json.getString("org") + "\n" +
                            "🌎 PAÍS: " + json.getString("country") + "\n" +
                            "📍 LOCAL: " + json.getString("city") + " - " + json.getString("regionName") + "\n" +
                            "📮 CEP: " + json.getString("zip") + "\n" +
                            "⏰ TIMEZONE: " + json.getString("timezone") + "\n" +
                            "📐 LAT: " + json.getString("lat") + "\n" +
                            "📐 LON: " + json.getString("lon") + "\n" +
                            "🛰️ ASN: " + json.getString("as") + "\n\n" +
                            ">> FIM DA TRANSMISSÃO.";

                    runOnUiThread(() -> {
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        txtResultado.setText(""); // Limpa o "Aguardando"
                        iniciarDigitacao();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> txtResultado.setText(">> ERRO: FALHA NA INTERCEPTAÇÃO DE DADOS."));
            }
        }).start();
    }

    private void iniciarDigitacao() {
        charIndex = 0;
        typewriterHandler.postDelayed(typewriterRunnable, 40);
    }

    private final Runnable typewriterRunnable = new Runnable() {
        @Override
        public void run() {
            if (charIndex < relatorioFinal.length()) {
                txtResultado.append(String.valueOf(relatorioFinal.charAt(charIndex)));
                charIndex++;
                typewriterHandler.postDelayed(this, 30); // Velocidade da digitação
            }
        }
    };
}
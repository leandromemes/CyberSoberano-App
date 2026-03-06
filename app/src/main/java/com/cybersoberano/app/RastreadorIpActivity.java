package com.cybersoberano.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RastreadorIpActivity extends AppCompatActivity {

    private TextView txtResultado;
    private ProgressBar progressBar;
    private EditText editIpAlvo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Certifique-se que o nome do XML abaixo está correto
        setContentView(R.layout.activity_rastreador_ip);

        // Mapeamento dos componentes (IDs ajustados para o seu XML)
        txtResultado = findViewById(R.id.txtResultadoFinal);
        progressBar = findViewById(R.id.progressoScan);
        editIpAlvo = findViewById(R.id.editIpAlvo);

        Button btnRastrear = findViewById(R.id.btnRastrear);
        Button btnCopiar = findViewById(R.id.btnCopiar);
        View btnVoltar = findViewById(R.id.btnVoltar);

        if (btnVoltar != null) btnVoltar.setOnClickListener(v -> finish());

        // Lógica do Botão Rastrear
        if (btnRastrear != null) {
            btnRastrear.setOnClickListener(v -> {
                String ip = (editIpAlvo != null) ? editIpAlvo.getText().toString().trim() : "";
                executarRastreioReal(ip);
            });
        }

        // Lógica do Botão Copiar
        if (btnCopiar != null) {
            btnCopiar.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("IP Report", txtResultado.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Relatório Copiado!", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void executarRastreioReal(String ipAlvo) {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        txtResultado.setText(">> [ INICIANDO RASTREIO AGRESSIVO ]\n");
        txtResultado.append(">> Conectando aos servidores WHOIS...\n");

        new Thread(() -> {
            try {
                // API gratuita para geolocalização
                URL url = new URL("http://ip-api.com/json/" + ipAlvo);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();

                JSONObject json = new JSONObject(result.toString());

                runOnUiThread(() -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    try {
                        if (json.getString("status").equals("success")) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("📍 IP ALVO: ").append(json.getString("query")).append("\n");
                            sb.append("🌍 PAÍS: ").append(json.getString("country")).append("\n");
                            sb.append("🏙️ CIDADE: ").append(json.getString("city")).append("\n");
                            sb.append("📡 PROVEDOR: ").append(json.getString("isp")).append("\n");
                            sb.append("🛰️ COORDENADAS: ").append(json.getString("lat")).append(", ").append(json.getString("lon")).append("\n");
                            sb.append("⏰ TIMEZONE: ").append(json.getString("timezone")).append("\n");
                            sb.append("----------------------------------\n");
                            sb.append(">> RASTREIO FINALIZADO.");
                            txtResultado.setText(sb.toString());
                        } else {
                            txtResultado.setText(">> ERRO: IP não encontrado ou privado.");
                        }
                    } catch (Exception e) {
                        txtResultado.setText(">> ERRO NA LEITURA DOS DADOS.");
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    txtResultado.setText(">> FALHA: Verifique sua conexão com a internet.");
                });
            }
        }).start();
    }
}

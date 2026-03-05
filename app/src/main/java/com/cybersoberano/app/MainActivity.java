package com.cybersoberano.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/* * Desenvolvido por Leandro | CyberSoberano
 * Painel de Elite com 5 Ferramentas e Auto-Update
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sistema de Alerta de Atualização
        verificarAtualizacaoSoberana();

        // 1. BOTÃO: NET SCAN
        Button btnNetScan = findViewById(R.id.btnNetScan);
        if (btnNetScan != null) {
            btnNetScan.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, NetScanActivity.class)));
        }

        // 2. BOTÃO: RASTREADOR DE IP
        Button btnRastreador = findViewById(R.id.btnHashGen);
        if (btnRastreador != null) {
            btnRastreador.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, RastreadorIpActivity.class)));
        }

        // 3. BOTÃO: VERIFICADOR DNS
        Button btnDns = findViewById(R.id.btnDNS);
        if (btnDns != null) {
            btnDns.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, DnsActivity.class)));
        }

        // 4. BOTÃO: CURSOS HACKER
        Button btnCursos = findViewById(R.id.btnCursos);
        if (btnCursos != null) {
            btnCursos.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, CursosActivity.class)));
        }

        // --- NOVO --- 5. BOTÃO: DIVULGAÇÃO DE GRUPOS [cite: 2026-03-02]
        // Certifique-se de que no seu activity_main.xml o ID do botão seja btnGrupos
        Button btnGrupos = findViewById(R.id.btnGrupos);
        if (btnGrupos != null) {
            btnGrupos.setOnClickListener(v ->
                    startActivity(new Intent(MainActivity.this, GruposCategoriasActivity.class)));
        }
    }

    private void verificarAtualizacaoSoberana() {
        new Thread(() -> {
            try {
                URL url = new URL("https://raw.githubusercontent.com/leandromemes/CyberSoberano-App/master/update.json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                JSONObject json = new JSONObject(sb.toString());
                int novaVersaoCode = json.getInt("versionCode");
                String mensagem = json.getString("message");
                String urlDownload = json.getString("url");

                int versaoAtualCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

                if (novaVersaoCode > versaoAtualCode) {
                    runOnUiThread(() -> {
                        new AlertDialog.Builder(this)
                                .setTitle("🚀 ATUALIZAÇÃO DISPONÍVEL")
                                .setMessage(mensagem)
                                .setCancelable(false)
                                .setPositiveButton("BAIXAR", (dialog, which) -> {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlDownload)));
                                })
                                .setNegativeButton("DEPOIS", null)
                                .show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
// Créditos: dev Leandro - CyberSoberano [cite: 2026-01-31]
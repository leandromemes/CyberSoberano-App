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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Chamada obrigatória para o sistema de update
        verificarAtualizacaoSoberana();

        // Configuração dos botões
        setupBotoes();
    }

    private void setupBotoes() {
        // Net Scan
        findViewById(R.id.btnNetScan).setOnClickListener(v ->
                startActivity(new Intent(this, NetScanActivity.class)));

        // Rastreador IP (Usando o ID btnHashGen do seu XML)
        findViewById(R.id.btnHashGen).setOnClickListener(v ->
                startActivity(new Intent(this, RastreadorIpActivity.class)));
    }

    private void verificarAtualizacaoSoberana() {
        new Thread(() -> {
            try {
                URL url = new URL("https://raw.githubusercontent.com/leandromemes/CyberSoberano-App/master/update.json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);

                JSONObject json = new JSONObject(sb.toString());
                int novaVersao = json.getInt("versionCode");
                String msg = json.getString("message");
                String link = json.getString("url");

                int versaoAtual = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

                if (novaVersao > versaoAtual) {
                    runOnUiThread(() -> new AlertDialog.Builder(this)
                            .setTitle("🚀 ATUALIZAÇÃO DISPONÍVEL")
                            .setMessage(msg)
                            .setPositiveButton("BAIXAR", (d, w) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link))))
                            .setNegativeButton("DEPOIS", null).show());
                }
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }
}
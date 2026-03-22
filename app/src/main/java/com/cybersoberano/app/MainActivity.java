package com.cybersoberano.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/** * Desenvolvido por Leandro | CyberSoberano
 * Dashboard Principal - Sistema Multi-Ferramentas [2026]
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CyberSoberano";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicia verificação de atualização em segundo plano
        verificarAtualizacaoSoberana();

        // Mapeamento dos botões (Redirecionam para as telas específicas)
        configurarBotao(R.id.btnNetScan, NetScanActivity.class);
        configurarBotao(R.id.btnHashGen, RastreadorIpActivity.class);
        configurarBotao(R.id.btnDNS, DnsActivity.class);
        configurarBotao(R.id.btnCursos, CursosActivity.class);
        configurarBotao(R.id.btnGrupos, GruposCategoriasActivity.class);

        // BOTÃO GÓTICA BOT (TERMINAL)
        configurarBotao(R.id.btnAlugarBot, TerminalActivity.class);
    }

    /**
     * Método otimizado para configurar cliques nos botões
     */
    private void configurarBotao(int id, Class<?> activityClass) {
        Button btn = findViewById(id);
        if (btn != null) {
            btn.setOnClickListener(v -> startActivity(new Intent(this, activityClass)));
        }
    }

    /**
     * Verifica se existe uma nova versão do APK no GitHub
     */
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
                    runOnUiThread(() -> new AlertDialog.Builder(this)
                        .setTitle("🚀 ATUALIZAÇÃO DISPONÍVEL")
                        .setMessage(mensagem)
                        .setCancelable(false)
                        .setPositiveButton("BAIXAR", (dialog, which) ->
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlDownload))))
                        .setNegativeButton("DEPOIS", null)
                        .show());
                }

            } catch (Exception e) {
                Log.e(TAG, "Erro na verificação de atualização: " + e.getMessage());
            }
        }).start();
    }
}

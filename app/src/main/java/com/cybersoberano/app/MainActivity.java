package com.cybersoberano.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
/* * * Desenvolvido por Leandro | CyberSoberano
 * Dashboard Principal - Sistema Multi-Ferramentas
=======
/* * Desenvolvido por Leandro | CyberSoberano
 * Painel de Elite - Código Otimizado [cite: 2026-01-28, 2026-03-02]
>>>>>>> origin/master
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CyberSoberano";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        // 1. Botão Net Scan
        Button btnNetScan = findViewById(R.id.btnNetScan);
        if (btnNetScan != null) {
            btnNetScan.setOnClickListener(v -> startActivity(new Intent(this, NetScanActivity.class)));
        }

        // 2. Botão Rastreador de IP (HashGen no XML)
        Button btnRastreador = findViewById(R.id.btnHashGen);
        if (btnRastreador != null) {
            btnRastreador.setOnClickListener(v -> startActivity(new Intent(this, RastreadorIpActivity.class)));
        }

        // 3. Botão Verificador DNS
        Button btnDns = findViewById(R.id.btnDNS);
        if (btnDns != null) {
            btnDns.setOnClickListener(v -> startActivity(new Intent(this, DnsActivity.class)));
        }

        // 4. Botão Academia Hacker (Cursos)
        Button btnCursos = findViewById(R.id.btnCursos);
        if (btnCursos != null) {
            btnCursos.setOnClickListener(v -> startActivity(new Intent(this, CursosActivity.class)));
        }

        // 5. Botão Gótica Bot (Terminal)
        Button btnBots = findViewById(R.id.btnAlugarBot);
        if (btnBots != null) {
            btnBots.setOnClickListener(v -> startActivity(new Intent(this, TerminalActivity.class)));
        }

        // 6. NOVO: Botão Divulgação de Grupos (WhatsApp)
        // Verifique se o ID no seu activity_main.xml é btnGrupos ou btnDivulgar
        Button btnGrupos = findViewById(R.id.btnGrupos);
        if (btnGrupos != null) {
            btnGrupos.setOnClickListener(v -> {
                startActivity(new Intent(this, GruposCategoriasActivity.class));
            });
        }
    }
=======
        verificarAtualizacaoSoberana();

        // Mapeamento dos botões com verificação de segurança [cite: 2026-03-02]
        configurarBotao(R.id.btnNetScan, NetScanActivity.class);
        configurarBotao(R.id.btnHashGen, RastreadorIpActivity.class);
        configurarBotao(R.id.btnDNS, DnsActivity.class);
        configurarBotao(R.id.btnCursos, CursosActivity.class);
        configurarBotao(R.id.btnGrupos, GruposCategoriasActivity.class);
        configurarBotao(R.id.btnAlugarBot, TerminalActivity.class);

        // Se ainda não criou a GeradorLinkActivity, comente a linha abaixo para não dar erro
        // configurarBotao(R.id.btnLinkWhats, GeradorLinkActivity.class);
    }

    // Método otimizado para configurar botões (Expression Lambda) [cite: 2026-03-02]
    private void configurarBotao(int id, Class<?> activityClass) {
        Button btn = findViewById(id);
        if (btn != null) {
            btn.setOnClickListener(v -> startActivity(new Intent(this, activityClass)));
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
                processarJsonAtualizacao(json);

            } catch (Exception e) {
                Log.e(TAG, "Erro na verificação de atualização: " + e.getMessage());
            }
        }).start();
    }

    private void processarJsonAtualizacao(JSONObject json) {
        try {
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
            Log.e(TAG, "Erro ao processar JSON: " + e.getMessage());
        }
    }
>>>>>>> origin/master
}

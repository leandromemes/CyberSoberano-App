package com.cybersoberano.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/* * * Desenvolvido por Leandro | CyberSoberano
 * Dashboard Principal - Sistema Multi-Ferramentas
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}

package com.cybersoberano.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/* * Desenvolvido por Leandro | CyberSoberano
 * Menu Principal de Elite - Conectando Ferramentas
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BOTÃO 1: NET SCAN
        Button btnNetScan = findViewById(R.id.btnNetScan);
        if (btnNetScan != null) {
            btnNetScan.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, NetScanActivity.class));
            });
        }

        // BOTÃO 2: RASTREADOR DE IP (ID btnHashGen no seu XML)
        Button btnRastreador = findViewById(R.id.btnHashGen);
        if (btnRastreador != null) {
            btnRastreador.setOnClickListener(v -> {
                // Aqui chamamos a nova tela de Geolocalização
                startActivity(new Intent(MainActivity.this, RastreadorIpActivity.class));
            });
        }
    }
}
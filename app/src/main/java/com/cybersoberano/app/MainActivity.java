package com.cybersoberano.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/* * Desenvolvido por Leandro | CyberSoberano
 * Menu Principal de Elite
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNetScan = findViewById(R.id.btnNetScan);
        if (btnNetScan != null) {
            btnNetScan.setOnClickListener(v -> {
                // Abre a tela de varredura
                startActivity(new Intent(MainActivity.this, NetScanActivity.class));
            });
        }
    }
}
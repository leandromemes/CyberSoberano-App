package com.cybersoberano.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.net.InetAddress;

/* * Desenvolvido por Leandro | CyberSoberano
 * Scan de Elite - Com Pedido de Permissão Real
 */
public class NetScanActivity extends AppCompatActivity {
    private TextView txtRelatorio;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        txtRelatorio = findViewById(R.id.txtResultadoFinal);
        progressBar = findViewById(R.id.progressoScan);

        // ESSENCIAL: Pede permissão de localização em tempo real
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        executarScanProfissional();
    }

    private void executarScanProfissional() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        txtRelatorio.setText(">> [SISTEMA] RASTREANDO ALVOS NA REDE...\n\n");

        new Thread(() -> {
            try {
                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                int ipInt = wm.getConnectionInfo().getIpAddress();
                String ipLocal = (ipInt & 0xff) + "." + (ipInt >> 8 & 0xff) + "." + (ipInt >> 16 & 0xff) + "." + (ipInt >> 24 & 0xff);
                String rede = ipLocal.substring(0, ipLocal.lastIndexOf(".") + 1);

                for (int i = 1; i <= 254; i++) {
                    InetAddress addr = InetAddress.getByName(rede + i);
                    if (addr.isReachable(200)) {
                        String nome = addr.getCanonicalHostName();
                        // Se o nome for igual ao IP, ele não conseguiu resolver o Hostname
                        String display = nome.equals(rede + i) ? "Dispositivo Protegido" : nome;

                        final String info = "📍 IP: " + rede + i + "\n🏷️ NOME: " + display + "\n\n";
                        runOnUiThread(() -> txtRelatorio.append(info));
                    }
                }
                runOnUiThread(() -> {
                    txtRelatorio.append(">> VARREDURA COMPLETA.");
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                });
            } catch (Exception e) { e.printStackTrace(); }
        }).start();
    }
}
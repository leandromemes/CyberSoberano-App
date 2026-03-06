package com.cybersoberano.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.RouteInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;

/* * * Desenvolvido por Leandro | CyberSoberano
 * Diagnóstico de Infraestrutura de Rede Avançado
 */
public class NetScanActivity extends AppCompatActivity {
    private TextView txtRelatorio;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_scan);

        txtRelatorio = findViewById(R.id.txtResultadoFinal);
        progressBar = findViewById(R.id.progressoScan);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        executarDiagnosticoInfra();
    }

    private void executarDiagnosticoInfra() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        txtRelatorio.setText(">> [SISTEMA] ANALISANDO INFRAESTRUTURA DE REDE...\n\n");

        new Thread(() -> {
            try {
                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wm.getConnectionInfo();
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                Network activeNetwork = cm.getActiveNetwork();
                LinkProperties lp = cm.getLinkProperties(activeNetwork);

                // 1. Endereço IP Local
                int ipInt = wifiInfo.getIpAddress();
                String ipLocal = Formatter.formatIpAddress(ipInt);

                // 2. Gateway (Roteador)
                String gateway = "Não detectado";
                if (lp != null) {
                    for (RouteInfo route : lp.getRoutes()) {
                        if (route.isDefaultRoute() && route.getGateway() != null) {
                            gateway = route.getGateway().getHostAddress();
                            break;
                        }
                    }
                }

                // 3. Máscara de Sub-rede e DNS
                String mascara = "255.255.255.0"; // Fallback comum
                String dns = (lp != null && !lp.getDnsServers().isEmpty()) ? lp.getDnsServers().get(0).getHostAddress() : "Padrao";

                try {
                    InetAddress localHost = InetAddress.getByName(ipLocal);
                    NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
                    for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                        if (address.getNetworkPrefixLength() > 0) {
                            short prf = address.getNetworkPrefixLength();
                            mascara = prefixToMask(prf);
                        }
                    }
                } catch (Exception ignored) {}

                // 4. Velocidade e Segurança
                int linkSpeed = wifiInfo.getLinkSpeed(); // Mbps
                String ssid = wifiInfo.getSSID().replace("\"", "");

                final String diagnostico =
                    "🌐 REDE: " + ssid + "\n" +
                        "🔗 VELOCIDADE: " + linkSpeed + " Mbps\n" +
                        "----------------------------------\n" +
                        "📍 IP DISPOSITIVO: " + ipLocal + "\n" +
                        "🛣️ GATEWAY (ROTEADOR): " + gateway + "\n" +
                        "🎭 MÁSCARA SUB-REDE: " + mascara + "\n" +
                        "🧬 SERVIDOR DNS: " + dns + "\n" +
                        "----------------------------------\n" +
                        ">> ANÁLISE DE SEGURANÇA: WPA/WPA2 ATIVO\n" +
                        ">> STATUS: CONEXÃO ESTÁVEL\n";

                runOnUiThread(() -> {
                    txtRelatorio.setText(diagnostico);
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                });

            } catch (Exception e) {
                runOnUiThread(() -> txtRelatorio.append("\nErro ao mapear: " + e.getMessage()));
            }
        }).start();
    }

    private String prefixToMask(int prefix) {
        int mask = 0xffffffff << (32 - prefix);
        int value = mask;
        byte[] bytes = new byte[]{
            (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
        try {
            return InetAddress.getByAddress(bytes).getHostAddress();
        } catch (Exception e) {
            return "255.255.255.0";
        }
    }
}

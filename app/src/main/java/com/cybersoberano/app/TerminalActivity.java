package com.cybersoberano.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class TerminalActivity extends AppCompatActivity {

    private TextView txtCodigoPareamento, txtStatusTerminal;
    private EditText editNumeroBot;
    private Button btnGerarCodigo;
    private DatabaseReference mDatabase;
    private ValueEventListener listenerAtual;
    private String numeroMonitorado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal);

        try {
            // Importante: Verifique se a URL do Firebase no google-services.json está correta
            mDatabase = FirebaseDatabase.getInstance().getReference("conexoes_whatsapp");
        } catch (Exception e) {
            Toast.makeText(this, "Erro Firebase!", Toast.LENGTH_LONG).show();
        }

        txtCodigoPareamento = findViewById(R.id.txtCodigoPareamento);
        txtStatusTerminal = findViewById(R.id.txtStatusTerminal);
        editNumeroBot = findViewById(R.id.editNumeroBot);
        btnGerarCodigo = findViewById(R.id.btnGerarCodigo);

        btnGerarCodigo.setOnClickListener(v -> solicitarConexao());
    }

    private void solicitarConexao() {
        String numeroRaw = editNumeroBot.getText().toString().trim();
        String numeroLimpo = numeroRaw.replaceAll("\\D", "");

        if (numeroLimpo.length() < 10) {
            Toast.makeText(this, "Digite DDD + Número!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!numeroLimpo.startsWith("55")) {
            numeroLimpo = "55" + numeroLimpo;
        }

        final String numeroFinal = numeroLimpo;
        this.numeroMonitorado = numeroFinal;

        btnGerarCodigo.setEnabled(false);
        btnGerarCodigo.setText("GERANDO...");

        txtStatusTerminal.setText(">> [SISTEMA] Solicitando código...");
        txtCodigoPareamento.setText("...");

        // USAR UPDATE EM VEZ DE SETVALUE PARA NÃO APAGAR O NÓ INTEIRO
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "solicitando");
        updates.put("requestPairing", true);
        updates.put("pairingCode", "gerando...");
        updates.put("updatedAt", System.currentTimeMillis());

        mDatabase.child(numeroFinal).updateChildren(updates).addOnSuccessListener(aVoid -> {
            txtStatusTerminal.append("\n>> [FIREBASE] Aguardando Bot...");
            ouvirResposta(numeroFinal);
        }).addOnFailureListener(e -> {
            txtStatusTerminal.append("\n>> [ERRO]: " + e.getMessage());
            btnGerarCodigo.setEnabled(true);
        });
    }

    private void ouvirResposta(String numero) {
        if (listenerAtual != null) {
            mDatabase.child(numero).removeEventListener(listenerAtual);
        }

        listenerAtual = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;

                // Pegamos os valores brutos para evitar erro de cast
                Object statusObj = snapshot.child("status").getValue();
                Object codigoObj = snapshot.child("pairingCode").getValue();

                String status = statusObj != null ? statusObj.toString() : "";
                String codigo = codigoObj != null ? codigoObj.toString() : "";

                // LOG DE DEBUG NO TERMINAL DO APP
                if (!codigo.isEmpty() && !codigo.equals("gerando...")) {
                    // Força a atualização na UI Thread
                    new Handler(Looper.getMainLooper()).post(() -> {
                        txtCodigoPareamento.setText(codigo);
                        if (!txtStatusTerminal.getText().toString().contains(codigo)) {
                            txtStatusTerminal.append("\n>> [BOT] CÓDIGO RECEBIDO!");
                        }
                    });
                }

                if ("conectado".equals(status)) {
                    txtCodigoPareamento.setText("OK");
                    txtStatusTerminal.append("\n>> [!] BOT ONLINE!");
                    btnGerarCodigo.setEnabled(true);
                    btnGerarCodigo.setText("CONECTADO");
                }

                if ("aguardando_codigo".equals(status)) {
                    txtStatusTerminal.append("\n>> [!] Código pronto no WhatsApp.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        mDatabase.child(numero).addValueEventListener(listenerAtual);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerAtual != null && !numeroMonitorado.isEmpty()) {
            mDatabase.child(numeroMonitorado).removeEventListener(listenerAtual);
        }
    }
}

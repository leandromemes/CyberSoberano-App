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

/* * * Desenvolvido por Leandro | CyberSoberano
 * Terminal de Pareamento - Sistema Multiconexão [2026]
 */
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
            // Inicializa a referência do Firebase
            mDatabase = FirebaseDatabase.getInstance().getReference("conexoes_whatsapp");
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao conectar com Firebase!", Toast.LENGTH_LONG).show();
        }

        // Mapeia os componentes do layout novo
        txtCodigoPareamento = findViewById(R.id.txtCodigoPareamento);
        txtStatusTerminal = findViewById(R.id.txtStatusTerminal);
        editNumeroBot = findViewById(R.id.editNumeroBot);
        btnGerarCodigo = findViewById(R.id.btnGerarCodigo);

        btnGerarCodigo.setOnClickListener(v -> solicitarConexao());
    }

    private void solicitarConexao() {
        String numeroRaw = editNumeroBot.getText().toString().trim();
        // Remove tudo que não for número
        String numeroLimpo = numeroRaw.replaceAll("\\D", "");

        if (numeroLimpo.length() < 10) {
            Toast.makeText(this, "Digite DDD + Número!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Garante o DDI 55 (Brasil)
        if (!numeroLimpo.startsWith("55")) {
            numeroLimpo = "55" + numeroLimpo;
        }

        final String numeroFinal = numeroLimpo;
        this.numeroMonitorado = numeroFinal;

        // Feedback visual no botão
        btnGerarCodigo.setEnabled(false);
        btnGerarCodigo.setText("GERANDO...");

        txtStatusTerminal.setText(">> [SISTEMA] Solicitando código...");
        txtCodigoPareamento.setText("...");

        // Prepara os dados para o Firebase disparar o Bot no PC
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "solicitando");
        updates.put("requestPairing", true);
        updates.put("pairingCode", "gerando...");
        updates.put("updatedAt", System.currentTimeMillis());

        mDatabase.child(numeroFinal).updateChildren(updates).addOnSuccessListener(aVoid -> {
            txtStatusTerminal.append("\n>> [FIREBASE] Aguardando resposta do Bot...");
            ouvirResposta(numeroFinal);
        }).addOnFailureListener(e -> {
            txtStatusTerminal.append("\n>> [ERRO]: " + e.getMessage());
            btnGerarCodigo.setEnabled(true);
            btnGerarCodigo.setText("TENTAR NOVAMENTE");
        });
    }

    private void ouvirResposta(String numero) {
        // Remove listener anterior se existir para evitar loops
        if (listenerAtual != null) {
            mDatabase.child(numero).removeEventListener(listenerAtual);
        }

        listenerAtual = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) return;

                Object statusObj = snapshot.child("status").getValue();
                Object codigoObj = snapshot.child("pairingCode").getValue();

                String status = statusObj != null ? statusObj.toString() : "";
                String codigo = codigoObj != null ? codigoObj.toString() : "";

                // Quando o Bot no PC gera o código, ele aparece aqui
                if (!codigo.isEmpty() && !codigo.equals("gerando...")) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        txtCodigoPareamento.setText(codigo);
                        if (!txtStatusTerminal.getText().toString().contains("CÓDIGO RECEBIDO")) {
                            txtStatusTerminal.append("\n>> [BOT] CÓDIGO RECEBIDO!");
                            txtStatusTerminal.append("\n>> Verifique a notificação do WhatsApp.");
                        }
                    });
                }

                // Quando o usuário digita o código no WhatsApp e conecta
                if ("conectado".equals(status)) {
                    txtCodigoPareamento.setText("OK");
                    txtStatusTerminal.append("\n>> [!] BOT CONECTADO COM SUCESSO!");
                    btnGerarCodigo.setEnabled(true);
                    btnGerarCodigo.setText("GERAR OUTRO");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                txtStatusTerminal.append("\n>> [ERRO DATABASE]: " + error.getMessage());
            }
        };

        mDatabase.child(numero).addValueEventListener(listenerAtual);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpa o monitoramento ao fechar a tela para economizar bateria e dados
        if (listenerAtual != null && !numeroMonitorado.isEmpty()) {
            mDatabase.child(numeroMonitorado).removeEventListener(listenerAtual);
        }
    }
}

package com.cybersoberano.app;

import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AdicionarGrupoActivity extends AppCompatActivity {

    private EditText editLink, editNome;
    private ImageView imgGrupo;
    private Spinner spinnerCategoria;
    private String urlImagemCapturada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_grupo);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        editLink = findViewById(R.id.editLink);
        editNome = findViewById(R.id.editNome);
        imgGrupo = findViewById(R.id.imgGrupo);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        Button btnConfirmar = findViewById(R.id.btnConfirmarDivulgacao);

        String[] categorias = {"Amizade", "Romance", "Zueira", "Outros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        editLink.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String url = s.toString().trim();
                if (url.contains("chat.whatsapp.com/")) {
                    puxarDadosDoWhatsApp(url);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        btnConfirmar.setOnClickListener(v -> validarESalvar());
    }

    private void puxarDadosDoWhatsApp(String url) {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(url).get();
                String nome = doc.select("meta[property=og:title]").attr("content");
                urlImagemCapturada = doc.select("meta[property=og:image]").attr("content");

                runOnUiThread(() -> {
                    if (nome != null) editNome.setText(nome);
                    Glide.with(this).load(urlImagemCapturada).into(imgGrupo);
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Erro ao ler link", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void validarESalvar() {
        String nome = editNome.getText().toString().trim();
        String link = editLink.getText().toString().trim();
        String cat = spinnerCategoria.getSelectedItem().toString();

        if (nome.isEmpty() || link.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Grupos");

        // Bloqueio de duplicatas: verifica se o link já existe [cite: 2026-03-02]
        db.orderByChild("link").equalTo(link).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(AdicionarGrupoActivity.this, "⚠️ Este grupo já foi adicionado!", Toast.LENGTH_SHORT).show();
                } else {
                    finalizarCadastro(db, nome, link, cat);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void finalizarCadastro(DatabaseReference db, String nome, String link, String cat) {
        String id = db.push().getKey();
        // Captura o ID do dispositivo para o sistema de "Dono" [cite: 2026-03-02]
        String donoId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        if (id != null) {
            Grupo novoGrupo = new Grupo(nome, link, urlImagemCapturada, cat, donoId);
            db.child(id).setValue(novoGrupo).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Grupo enviado com sucesso! ✅", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
}
// Créditos: dev Leandro - CyberSoberano [cite: 2026-01-31]
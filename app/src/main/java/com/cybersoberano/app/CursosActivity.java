package com.cybersoberano.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class CursosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Certifique-se que o arquivo na pasta layout se chama activity_cursos.xml
        setContentView(R.layout.activity_cursos);

        if (findViewById(R.id.btnVoltar) != null) {
            findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        }

        LinearLayout container = findViewById(R.id.containerCursos);
        if (container != null) {
            adicionarCurso(container, "🛡️ Blue Team, Red Team e Hardware", "https://t.me/FoxHackers/6930");
            adicionarCurso(container, "🕵️ Pentest Profissional", "https://t.me/FoxHackers/6355");
            adicionarCurso(container, "📊 Become a SOC Analyst", "https://t.me/FoxHackers/5556");
            adicionarCurso(container, "🎓 SecVox Academy (Parte 1)", "https://t.me/FoxHackers/5295");
            adicionarCurso(container, "🎓 SecVox Academy (Parte 2)", "https://t.me/FoxHackers/5523");
            adicionarCurso(container, "🧨 Metasploit & Exploração", "https://t.me/FoxHackers/5209");
            adicionarCurso(container, "🤖 Mr. Robot Full Course", "https://t.me/FoxHackers/4969");
            adicionarCurso(container, "🏫 Universidade Hacker", "https://t.me/FoxHackers/4771");
            adicionarCurso(container, "📜 CEH Certified Ethical Hacker", "https://t.me/FoxHackers/4347");
            adicionarCurso(container, "🎖️ CEH Elite Edition", "https://t.me/FoxHackers/4458");
            adicionarCurso(container, "💻 Especialista JPA-Algaworks", "https://t.me/FoxHackers/3753");
            adicionarCurso(container, "🔐 Formação Cybersegurança", "https://t.me/FoxHackers/1516");
            adicionarCurso(container, "📡 WiFi Hacking Enterprise", "https://t.me/FoxHackers/1393");
            adicionarCurso(container, "🔍 Hacker Investigador", "https://t.me/FoxHackers/1164");
        }
    }

    private void adicionarCurso(LinearLayout container, String nome, String url) {
        Button btn = new Button(this);

        // CORREÇÃO: MATCH_PARENT deve ser em MAIÚSCULO
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 180);
        params.setMargins(0, 15, 0, 15);

        btn.setLayoutParams(params);
        btn.setText(nome);
        btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF111111));
        btn.setTextColor(0xFF00FF00);
        btn.setAllCaps(false);

        btn.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        });

        container.addView(btn);
    }
}